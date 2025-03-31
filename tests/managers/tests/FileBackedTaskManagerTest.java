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

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    private FileBackedTaskManager manager;
    private File tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        //Создаем временный файл для тестов
        tempFile = File.createTempFile("task_manager_test", ".csv");
        manager = new FileBackedTaskManager(tempFile);
    }

    @Test
    public void SaveAndLoadEmptyFileTest() {
        //Сохраняем пустой менеджер
        manager.save();

        //Загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        //Проверяем, что загруженный менеджер пустой
        assertTrue(loadedManager.getTaskList().isEmpty());
        assertTrue(loadedManager.getEpicList().isEmpty());
        assertTrue(loadedManager.getSubtasksList().isEmpty());
    }

    @Test
    public void SaveMultipleTasksTest() {
        //Добавляем несколько задач
        Task task1 = new Task("Task1", "Description1", 1, Status.NEW);
        Task task2 = new Task("Task2", "Description2", 2, Status.DONE);
        Epic epic = new Epic("Epic1", "Epic Description", 3, Status.NEW);
        Subtask subtask1 = new Subtask(
                "Subtask1",
                "Subtask Description1",
                4,
                Status.NEW,
                epic.getId()
        );
        Subtask subtask2 = new Subtask(
                "Subtask2",
                "Subtask Description2",
                5,
                Status.DONE,
                epic.getId()
        );

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        //Загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        //Проверяем, что загруженные задачи соответствуют добавленным
        assertEquals(2, loadedManager.getTaskList().size());
        assertEquals(1, loadedManager.getEpicList().size());
        assertEquals(2, loadedManager.getSubtasksList().size());

        //Проверяем содержимое задач
        assertEquals("Task1", loadedManager.getTaskList().get(0).getName());
        assertEquals("Task2", loadedManager.getTaskList().get(1).getName());
        assertEquals("Epic1", loadedManager.getEpicList().get(0).getName());
        assertEquals("Subtask1", loadedManager.getSubtasksList().get(0).getName());
        assertEquals("Subtask2", loadedManager.getSubtasksList().get(1).getName());
    }

    @Test
    public void LoadMultipleTasksTest() {
        //Сохраняем несколько задач в файл
        StringBuilder sb = new StringBuilder();
        sb.append("id,type,name,status,description,epic\n");
        sb.append("1,TASK,Task1,NEW,Description1,\n");
        sb.append("2,TASK,Task2,DONE,Description2,\n");
        sb.append("3,EPIC,Epic1,NEW,Epic Description,\n");
        sb.append("4,SUBTASK,Subtask1,NEW,Subtask Description1,3\n");
        sb.append("5,SUBTASK,Subtask2,DONE,Subtask Description2,3\n");

        boolean writeSuccess = true; //Флаг для отслеживания успешности записи

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(sb.toString());
        } catch (IOException e) {
            System.out.println("Не удалось записать в файл: " + e.getMessage());
            writeSuccess = false; //Устанавливаем флаг в false, если произошла ошибка в записи
        }

        //Проверяем, что запись прошла успешно
        assertTrue(writeSuccess, "Ошибка при записи в файл");

        //Загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        //Проверяем, что загруженные задачи соответствуют добавленным
        assertEquals(2, loadedManager.getTaskList().size());
        assertEquals(1, loadedManager.getEpicList().size());
        assertEquals(2, loadedManager.getSubtasksList().size());
        //Проверяем содержимое задач
        assertEquals("Task1", loadedManager.getTaskList().get(0).getName());
        assertEquals("Task2", loadedManager.getTaskList().get(1).getName());
        assertEquals("Epic1", loadedManager.getEpicList().get(0).getName());
        assertEquals("Subtask1", loadedManager.getSubtasksList().get(0).getName());
        assertEquals("Subtask2", loadedManager.getSubtasksList().get(1).getName());
    }
}