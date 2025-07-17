package tasks.tests;

import enums.Status;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private TaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    public void areEqualEpicsEqual() {
        Epic epic = new Epic("", "");
        Epic epic2 = new Epic("ww", "w");
        epic.setId(1);
        epic2.setId(1);
        assertEquals(epic, epic2, "Не равны! Ошибка.");
    }

    @Test
    public void epicStatusShouldBeDoneWhenAllSubtasksDone() {
        Epic epic = new Epic("Epic", "Description");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Sub", "Desc", epic.getId());
        subtask.setStatus(Status.DONE);
        manager.addSubtask(subtask);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void epicStatusShouldBeInProgressWhenMixedStatuses() {
        Epic epic = new Epic("Epic", "Description");
        manager.addEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "Desc", epic.getId());
        sub1.setStatus(Status.NEW);
        manager.addSubtask(sub1);

        Subtask sub2 = new Subtask("Sub2", "Desc", epic.getId());
        sub2.setStatus(Status.DONE);
        manager.addSubtask(sub2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void epicStatusShouldBeInProgressWhenAnySubtaskInProgress() {
        Epic epic = new Epic("Epic", "Description");
        manager.addEpic(epic);

        Subtask sub = new Subtask("Sub", "Desc", epic.getId());
        sub.setStatus(Status.IN_PROGRESS);
        manager.addSubtask(sub);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void epicStatusShouldBeNewWhenNoSubtasks() {
        Epic epic = new Epic("Epic", "Description");
        manager.addEpic(epic);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void epicStatusShouldBeNewWhenAllSubtasksNew() {
        Epic epic = new Epic("Epic", "Description");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Sub", "Desc", epic.getId());
        manager.addSubtask(subtask);
        assertEquals(Status.NEW, epic.getStatus());
    }
}