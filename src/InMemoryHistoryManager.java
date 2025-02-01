import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> historyList = new ArrayList<>();
    private static final int MAX_SPACE = 10;

    @Override
    public void addTask(Task task) {
        if (historyList.size() == MAX_SPACE) {
            historyList.removeFirst();
        }
        historyList.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
