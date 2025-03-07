package tests;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import enums.Status;
import tasks.Subtask;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private static TaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    public void areEqualEpicsEqual() {

        Epic epic = new Epic("", "", 1, Status.NEW);
        Epic epic2 = new Epic("ww", "w", 1, Status.NEW);

        assertEquals(epic, epic2, "Не равны! Ошибка.");
    }

    @Test
    public void deletedSubtasksIdCheck() {
        Epic epic = new Epic("Вытри", "Нос", 1, Status.NEW);
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Возьми", "Салфетку", 2, Status.NEW, 1);
        List<Subtask> subtaskList = new ArrayList<>();
        subtaskList.add(subtask);
        manager.addSubtask(subtask);

        Assertions.assertEquals(subtaskList, manager.getEpicsSubtasks(epic));

        manager.deleteSubtaskById(2);
        subtaskList.clear();

        Assertions.assertEquals(subtaskList, manager.getEpicsSubtasks(epic));
    }
}