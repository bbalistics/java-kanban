import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    public void areEqualEpicsEqual() {
        Epic epic = new Epic("", "", 1, Status.NEW);
        Epic epic2 = new Epic("ww", "w", 1, Status.NEW);

        assertEquals(epic, epic2, "Не равны! Ошибка.");
    }
}