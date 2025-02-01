import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int id = 1;
    private HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasksList() {
        return new ArrayList<> (subtasks.values());
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.addTask(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.addTask(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.addTask(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();

        for (Epic epic : epics.values()) {
            epic.deleteAllSubtaskId();
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public void addTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtaskId(subtask);
        checkEpicStatus(epic);
    }

    @Override
    public void updateTask(Task task) {
        tasks.replace(task.getId(), task);
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic oldEpic = epics.get(epic.getId());
        ArrayList<Integer> oldSubtasks = oldEpic.getSubtaskListId();
        if (!oldSubtasks.isEmpty()) {
            for (Integer subtaskId : oldEpic.getSubtaskListId()) {
                if (!epic.getSubtaskListId().contains(subtaskId)) {
                    subtasks.remove(subtaskId);
                }
            }
        }

        epics.replace(epic.getId(), epic);
        checkEpicStatus(epic);
        return epic;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.replace(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        checkEpicStatus(epic);
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        ArrayList<Integer> epicSubtasks = epics.get(id).getSubtaskListId();
        epics.remove(id);
        for (Integer subtaskId : epicSubtasks) {
            subtasks.remove(subtaskId);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        epic.deleteSubtaskId(id);
        subtasks.remove(id);
        checkEpicStatus(epic);
    }

    @Override
    public ArrayList<Subtask> getEpicsSubtasks(Epic epic) {
        ArrayList<Subtask> epicsSubtasks = new ArrayList<>();

        for (Integer subtaskId : epic.getSubtaskListId()) {
            epicsSubtasks.add(subtasks.get(subtaskId));
        }
        return epicsSubtasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private int generateId() {
        return id++;
    }

    private void checkEpicStatus(Epic epic) {
        boolean isNew = false;
        boolean isDone = false;
        int countNew = 0;
        int countDone = 0;

        if (subtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        for (Integer subtaskId : epic.getSubtaskListId()) {
            if (subtasks.get(subtaskId).getStatus() == Status.NEW && countDone == 0) {
                countNew++;
            } else if (subtasks.get(subtaskId).getStatus() == Status.DONE && countNew == 0) {
                countDone++;
            } else {
                break;
            }
        }

        if (countDone == epic.getSubtaskListId().size()) {
            isDone = true;
        } else if (countNew == epic.getSubtaskListId().size()) {
            isNew = true;
        }

        if (epic.getSubtaskListId().isEmpty() || isNew) {
            epic.setStatus(Status.NEW);
        } else if (isDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
