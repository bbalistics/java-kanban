package managers.tests;

import enums.Status;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class InMemoryHistoryManagerTest {
    private static TaskManager manager;
    private static final LocalDateTime TEST_TIME = LocalDateTime.now();
    private static final Duration TEST_DURATION = Duration.ofHours(1);

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    public void getHistorySaveOldTaskInfo() {
        Task task = new Task("Вынеси мусор", "Возьми пакеты", 1, Status.NEW,
                TEST_TIME, TEST_DURATION);
        manager.addTask(task);
        manager.getTaskById(1);
        manager.updateTask(new Task("Бездельничай", "скорее",
                1, Status.IN_PROGRESS,
                TEST_TIME.plusHours(1), TEST_DURATION));

        List<Task> tasks = manager.getHistory();
        Task oldTask = tasks.getFirst();

        //Проверяем, совпадают ли поля старого таска с его первоначальной версией
        Assertions.assertEquals(task.getName(), oldTask.getName());
        Assertions.assertEquals(task.getStatus(), oldTask.getStatus());
        Assertions.assertEquals(task.getDescription(), oldTask.getDescription());
        Assertions.assertEquals(task.getId(), oldTask.getId());

        Assertions.assertEquals(TEST_TIME, oldTask.getStartTime());
        Assertions.assertEquals(TEST_DURATION, oldTask.getDuration());
        Assertions.assertEquals(TEST_TIME.plus(TEST_DURATION), oldTask.getEndTime());
    }

    @Test
    public void addTaskWork() {
        //Создаем проверочный список, добавляем туда два таска, вызываем их по очереди

        Task task = new Task("Вынеси мусор", "Возьми пакеты");
        manager.addTask(task);
        manager.getTaskById(1);

        Task task2 = new Task("не выноси мусор", "не бери пакеты");
        manager.addTask(task2);
        manager.getTaskById(2);

        List<Task> testTasks = new ArrayList<>();
        testTasks.add(task);
        testTasks.add(task2);

        Assertions.assertEquals(testTasks, manager.getHistory());
    }

    @Test
    public void linkLastWork() {
        //Создаем проверочный список, добавляем туда два таска, вызываем их по очереди

        Task task = new Task("Вынеси мусор", "Возьми пакеты");
        manager.addTask(task);
        manager.getTaskById(1);

        Task task2 = new Task("не выноси мусор", "не бери пакеты");
        manager.addTask(task2);
        manager.getTaskById(2);

        List<Task> testTasks = new ArrayList<>();
        testTasks.add(task);
        testTasks.add(task2);

        Assertions.assertEquals(testTasks, manager.getHistory());


        //Вызываем первый таск

        manager.getTaskById(1);
        testTasks.removeFirst();
        testTasks.add(task);

        Assertions.assertEquals(testTasks, manager.getHistory());
    }
}