package managers.tests;

import enums.Status;
import managers.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createManager() {
        return new InMemoryTaskManager();
    }

    @Test
    public void taskAddedIsEqualToTaskCreated() {
        Task taskCreated = new Task("Вытри стол","тряпкой", 0,
                Status.NEW, null, null);
        manager.addTask(taskCreated);

        Task taskFromManager = manager.getTaskById(taskCreated.getId());
        assertEquals(taskCreated.getName(), taskFromManager.getName());
        assertEquals(taskCreated.getDescription(), taskFromManager.getDescription());
        assertEquals(taskCreated.getId(), taskFromManager.getId());
        assertEquals(taskCreated.getStatus(), taskFromManager.getStatus());
    }
}