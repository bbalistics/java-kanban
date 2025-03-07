package tests;

import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;
import managers.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void getDefault() {
        assertInstanceOf(InMemoryTaskManager.class, Managers.getDefault(), "getDefault не работает.");
    }

    @Test
    public void getDefaultHistory() {
        assertInstanceOf(InMemoryHistoryManager.class, Managers.getDefaultHistory(),
                "getDefaultHistory не работает.");
    }
}