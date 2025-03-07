package test;

import enums.Status;
import managers.InMemoryTaskManager;
import managers.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private static InMemoryTaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    public void addTask() {
        Task task = new Task("test", "test");

        manager.addTask(task);

        assertNotNull(manager.getTaskList());
        assertEquals(task, manager.getTaskById(1));
    }

    @Test
    public void addEpic() {
        Epic epic = new Epic("test", "test");

        manager.addEpic(epic);

        assertNotNull(manager.getEpicList());
        assertEquals(epic, manager.getEpicById(1));
    }

    @Test
    public void addSubtask() {
        Epic epic = new Epic("test", "test");
        Subtask subtask = new Subtask("test", "test", 1);

        manager.addEpic(epic);
        manager.addSubtask(subtask);

        assertNotNull(manager.getSubtasksList());
        assertEquals(subtask, manager.getSubtaskById(2));
    }

    @Test
    public void taskAddedIsEqualToTaskCreated() {
        Task taskCreated = new Task("Вытри стол","тряпкой", 1, Status.NEW);
        manager.addTask(taskCreated);

        assertEquals(taskCreated.getName(), manager.getTaskById(1).getName());
        assertEquals(taskCreated.getDescription(), manager.getTaskById(1).getDescription());
        assertEquals(taskCreated.getId(), manager.getTaskById(1).getId());
        assertEquals(taskCreated.getStatus(), manager.getTaskById(1).getStatus());
    }
}