package managers.tests;

import enums.Status;
import managers.FileBackedTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    private FileBackedTaskManager manager;
    private File tempFile;
    private final LocalDateTime testTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    private final Duration testDuration = Duration.ofMinutes(30);

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = File.createTempFile("task_manager_test", ".csv");
        manager = new FileBackedTaskManager(tempFile);
    }

    @Test
    public void saveAndLoadEmptyFileTest() {
        manager.save();
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loadedManager.getTaskList().isEmpty());
        assertTrue(loadedManager.getEpicList().isEmpty());
        assertTrue(loadedManager.getSubtasksList().isEmpty());
    }

    @Test
    public void saveMultipleTasksWithTimeTest() {
        //Создаем таски, специально указываем plusHours для предотвращения ошибок

        Task task1 = new Task("Task1", "Description1", 1, Status.NEW,
                testTime, testDuration);
        Task task2 = new Task("Task2", "Description2", 2, Status.DONE,
                testTime.plusHours(1), testDuration);
        Epic epic = new Epic("Epic1", "Epic Description", 3, Status.NEW);
        Subtask subtask1 = new Subtask(
                "Subtask1",
                "Subtask Description1",
                4,
                Status.NEW,
                testTime.plusHours(2),
                testDuration,
                epic.getId()
        );
        Subtask subtask2 = new Subtask(
                "Subtask2",
                "Subtask Description2",
                5,
                Status.DONE,
                testTime.plusHours(3),
                testDuration,
                epic.getId()
        );

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(2, loadedManager.getTaskList().size());
        assertEquals(1, loadedManager.getEpicList().size());
        assertEquals(2, loadedManager.getSubtasksList().size());

        assertEquals(testTime, loadedManager.getTaskList().get(0).getStartTime().truncatedTo(ChronoUnit.MINUTES));
        assertEquals(testDuration, loadedManager.getTaskList().get(0).getDuration());
        assertEquals(
                testTime.plus(testDuration),
                loadedManager.getTaskList().get(0).getEndTime().truncatedTo(ChronoUnit.MINUTES)
        );

        assertEquals(
                testTime.plusHours(2),
                loadedManager.getSubtasksList().get(0).getStartTime().truncatedTo(ChronoUnit.MINUTES)
        );
        assertEquals(testDuration, loadedManager.getSubtasksList().get(0).getDuration());

        assertEquals(
                testTime.plusHours(2),
                loadedManager.getEpicList().get(0).getStartTime().truncatedTo(ChronoUnit.MINUTES)
        );
        assertEquals(Duration.ofMinutes(60), loadedManager.getEpicList().get(0).getDuration());
        assertEquals(
                testTime.plusHours(3).plusMinutes(30),
                loadedManager.getEpicList().get(0).getEndTime().truncatedTo(ChronoUnit.MINUTES)
        );
    }

    @Test
    public void loadMultipleTasksWithTimeTest() {
        StringBuilder sb = new StringBuilder();
        sb.append("id,type,name,status,description,startTime,duration,epic\n");
        sb.append("1,TASK,Task1,NEW,Description1,")
                .append(testTime).append(",").append(testDuration).append(",\n");
        sb.append("2,TASK,Task2,DONE,Description2,")
                .append(testTime.plusHours(2)).append(",").append(testDuration).append(",\n");
        sb.append("3,EPIC,Epic1,NEW,Epic Description,,,\n");
        sb.append("4,SUBTASK,Subtask1,NEW,Subtask Description1,")
                .append(testTime.plusHours(1)).append(",").append(Duration.ofMinutes(30)).append(",3\n");
        sb.append("5,SUBTASK,Subtask2,DONE,Subtask Description2,")
                .append(testTime.plusHours(3)).append(",").append(Duration.ofMinutes(45)).append(",3\n");

        boolean writeSuccess = true;
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(sb.toString());
        } catch (IOException e) {
            writeSuccess = false;
        }

        assertTrue(writeSuccess, "Ошибка при записи в файл");

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(2, loadedManager.getTaskList().size());
        assertEquals(1, loadedManager.getEpicList().size());
        assertEquals(2, loadedManager.getSubtasksList().size());

        //Проверка временных параметров
        assertEquals(testTime, loadedManager.getTaskList().get(0).getStartTime());
        assertEquals(testDuration, loadedManager.getTaskList().get(0).getDuration());

        assertEquals(testTime.plusHours(1), loadedManager.getSubtasksList().get(0).getStartTime());
        assertEquals(Duration.ofMinutes(30), loadedManager.getSubtasksList().get(0).getDuration());

        //Проверка расчетных полей эпика
        assertEquals(testTime.plusHours(1), loadedManager.getEpicList().get(0).getStartTime());
        assertEquals(Duration.ofHours(1).plusMinutes(15), loadedManager.getEpicList().get(0).getDuration());
        assertEquals(testTime.plusHours(3).plusMinutes(45), loadedManager.getEpicList().get(0).getEndTime());
    }

    @Test
    public void saveAndLoadTasksWithoutTimeTest() {
        Task taskWithoutTime = new Task("NoTime",
                "No time task", 1, Status.NEW, null, null);
        Epic epicWithoutTime = new Epic("NoTimeEpic", "No time epic", 2, Status.NEW);
        Subtask subtaskWithoutTime = new Subtask("NoTimeSubtask", "No time subtask", 3,
                Status.NEW, null, null, epicWithoutTime.getId());

        manager.addTask(taskWithoutTime);
        manager.addEpic(epicWithoutTime);
        manager.addSubtask(subtaskWithoutTime);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(1, loadedManager.getTaskList().size());
        assertEquals(1, loadedManager.getEpicList().size());
        assertEquals(1, loadedManager.getSubtasksList().size());

        assertNull(loadedManager.getTaskList().get(0).getStartTime());
        assertNull(loadedManager.getTaskList().get(0).getDuration());
        assertNull(loadedManager.getSubtasksList().get(0).getStartTime());
        assertNull(loadedManager.getSubtasksList().get(0).getDuration());
        assertNull(loadedManager.getEpicList().get(0).getStartTime());
        assertNull(loadedManager.getEpicList().get(0).getDuration());
    }
}