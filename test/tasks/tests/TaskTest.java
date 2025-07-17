package tasks.tests;

import enums.Status;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private static final LocalDateTime TEST_TIME = LocalDateTime.now();
    private static final Duration TEST_DURATION = Duration.ofMinutes(30);

    @Test
    public void areEqualTasksEqual() {
        Task task1 = new Task("Продай гараж", "время до завтра", 1, Status.NEW,
                TEST_TIME, TEST_DURATION);
        Task task2 = new Task("Купи гараж", "время до вчера", 1, Status.NEW,
                TEST_TIME, TEST_DURATION);

        assertEquals(task1, task2, "Не равны! Ошибка.");
    }

    @Test
    public void shouldHandleNullTimeValues() {
        Task task = new Task("Без времени", "тест", 1, Status.NEW, null, null);

        assertNull(task.getStartTime());
        assertNull(task.getDuration());
        assertNull(task.getEndTime());
    }

    @Test
    public void shouldCalculateEndTimeCorrectly() {
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 10, 0);
        Duration duration = Duration.ofHours(3);
        Task task = new Task("Тест", "Описание", 1, Status.NEW, startTime, duration);

        assertEquals(startTime.plus(duration), task.getEndTime());
    }
}