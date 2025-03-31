package managers.tests;

import enums.Status;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

class InMemoryHistoryManagerTest {
    private static TaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    public void getHistorySaveOldTaskInfo() {
        Task task = new Task("Вынеси мусор", "Возьми пакеты");
        manager.addTask(task);
        manager.getTaskById(1);
        manager.updateTask(new Task("Бездельничай", "скорее", task.getId(), Status.NEW));

        List<Task> tasks = manager.getHistory();
        Task oldTask = tasks.getFirst();

        Assertions.assertEquals(task.getName(), oldTask.getName());
        Assertions.assertEquals(task.getStatus(), oldTask.getStatus());
        Assertions.assertEquals(task.getDescription(), oldTask.getDescription());
        Assertions.assertEquals(task.getId(), oldTask.getId());
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