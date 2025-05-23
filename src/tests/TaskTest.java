package tests;

import enums.Status;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    public void areEqualTasksEqual(){
        Task task1 = new Task("Продай гараж", "время до завтра", 1, Status.NEW);
        Task task2 = new Task("Купи гараж", "время до вчера", 1, Status.NEW);

        assertEquals(task1, task2, "Не равны! Ошибка.");
    }
}