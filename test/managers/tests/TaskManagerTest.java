package managers.tests;

import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    protected abstract T createManager();

    @BeforeEach
    public void setUp() {
        manager = createManager();
    }

    @Test
    public void addTask() {
        Task task = new Task("test", "test");
        manager.addTask(task);
        assertNotNull(manager.getTaskList());
        assertEquals(task, manager.getTaskById(task.getId()));
    }

    @Test
    public void addEpic() {
        Epic epic = new Epic("test", "test");
        manager.addEpic(epic);
        assertNotNull(manager.getEpicList());
        assertEquals(epic, manager.getEpicById(epic.getId()));
    }

    @Test
    public void addSubtask() {
        Epic epic = new Epic("test", "test");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("test", "test", epic.getId());
        manager.addSubtask(subtask);
        assertNotNull(manager.getSubtasksList());
        assertEquals(subtask, manager.getSubtaskById(subtask.getId()));
    }
}
