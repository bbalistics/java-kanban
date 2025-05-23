package tasks.tests;

import enums.Status;
import org.junit.jupiter.api.Test;
import tasks.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    private static final LocalDateTime TEST_TIME = LocalDateTime.now();
    private static final Duration TEST_DURATION = Duration.ofMinutes(30);

    @Test
    public void areEqualSubtasksEqual() {
        Subtask subtask = new Subtask("Тест","тест",1, Status.NEW,
                TEST_TIME, TEST_DURATION, 1);
        Subtask subtask2 = new Subtask("Тест.","тест.",1, Status.NEW,
                TEST_TIME, TEST_DURATION, 1);

        assertEquals(subtask, subtask2, "Не равны! Ошибка.");
    }

    @Test
    public void shouldCalculateEndTimeCorrectly() {
        LocalDateTime startTime = LocalDateTime.of(2023, 1, 1, 12, 0);
        Duration duration = Duration.ofHours(2);
        Subtask subtask = new Subtask("Тест", "описание", 1, Status.NEW,
                startTime, duration, 1);

        assertEquals(startTime.plus(duration), subtask.getEndTime());
    }
}