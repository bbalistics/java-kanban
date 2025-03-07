package test;

import enums.Status;
import org.junit.jupiter.api.Test;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    public void areEqualSubtasksEqual() {
        Subtask subtask = new Subtask("Тест","тест",1, Status.NEW, 1);
        Subtask subtask2 = new Subtask("Тест.","тест.",1, Status.NEW, 1);

        assertEquals(subtask, subtask2, "Не равны! Ошибка.");
    }
}