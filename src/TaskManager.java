import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getTaskList();

    ArrayList<Epic> getEpicList();

    ArrayList<Subtask> getSubtasksList();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void updateTask(Task task);

    Epic updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    ArrayList<Subtask> getEpicsSubtasks(Epic epic);

    List<Task> getHistory();
}

