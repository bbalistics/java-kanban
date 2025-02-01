import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        manager.updateTask(new Task("Бездельничай", "скорее", task.getId(),Status.NEW));

        List<Task> tasks = manager.getHistory();
        Task oldTask = tasks.getFirst();

        assertEquals(task.getName(), oldTask.getName());
        assertEquals(task.getStatus(), oldTask.getStatus());
        assertEquals(task.getDescription(), oldTask.getDescription());
        assertEquals(task.getId(), oldTask.getId());
    }
}