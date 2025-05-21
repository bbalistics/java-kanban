package managers.tests;

import enums.Status;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TimeOverlapTest {
    private static final LocalDateTime BASE_TIME = LocalDateTime.now();
    private TaskManager manager;
    
    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    public void shouldDetectOverlappingTasks() {
        Task task1 = new Task("Task1", "dsk", 0, Status.NEW,
                BASE_TIME, Duration.ofHours(1));
        Task task2 = new Task("Task2", "dsk", 1, Status.NEW,
                BASE_TIME.plusMinutes(30), Duration.ofHours(1));

        assertTrue(manager.isTasksOverlap(task1, task2));
    }

    @Test
    public void shouldNotDetectNonOverlappingTasks() {
        Task task1 = new Task("Task1", "dsk", 0, Status.NEW,
                BASE_TIME, Duration.ofHours(1));
        Task task2 = new Task("Task2", "dsk", 1, Status.NEW,
                BASE_TIME.plusHours(2), Duration.ofHours(1));

        assertFalse(manager.isTasksOverlap(task1, task2));
    }

    @Test
    public void shouldHandleTasksWithoutTime() {
        Task task1 = new Task("Task1", "dsk");
        Task task2 = new Task("Task2", "dsk", 0, Status.NEW,
                BASE_TIME, Duration.ofHours(1));

        assertFalse(manager.isTasksOverlap(task1, task2));
    }
}
