package tests;

import enums.Status;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

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
}