import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int id = 1;

    public int generateId() {
        return id++;
    }

    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasksList() {
        return new ArrayList<> (subtasks.values());
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    public void addTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    public void addSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask);
        checkEpicStatus(epic);
    }

    public void updateTask(Task task) {
        tasks.replace(task.getId(), task);
    }

    public Epic updateEpic(Epic epic) {
        Epic oldEpic = epics.get(epic.getId());
        ArrayList<Subtask> oldSubtasks = oldEpic.getSubtaskList();
        if (!oldSubtasks.isEmpty()) {
            for (Subtask subtask : oldEpic.getSubtaskList()) {
                subtasks.remove(subtask.getId());
            }
        }

        ArrayList<Subtask> newSubtasks = epic.getSubtaskList();

        if (!newSubtasks.isEmpty()) {
            for (Subtask subtask : newSubtasks) {
                subtasks.put(subtask.getId(), subtask);
            }
        }

        epics.replace(epic.getId(), epic);
        checkEpicStatus(epic);
        return epic;
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.replace(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        ArrayList<Subtask> subtaskList = epic.getSubtaskList();
        subtaskList.remove(subtasks.get(subtask.getId()));
        subtaskList.add(subtask);
        epic.setSubtaskList(subtaskList);
        checkEpicStatus(epic);
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        ArrayList<Subtask> epicSubtasks = epics.get(id).getSubtaskList();
        epics.remove(id);
        for (Subtask subtask : epicSubtasks) {
            subtasks.remove(subtask.getId());
        }
    }

    public void deleteSubtaskById(int id) {
        subtasks.remove(id);
    }

    public ArrayList<Subtask> getEpicsSubtasks(Epic epic) {
        return epic.getSubtaskList();
    }

    private void checkEpicStatus(Epic epic) {
        boolean isNew = false;
        boolean isDone = false;
        int countNew = 0;
        int countDone = 0;

        for (Subtask subtask : epic.getSubtaskList()) {
            if (subtask.getStatus() == Status.NEW && countDone == 0) {
                countNew++;
            } else if (subtask.getStatus() == Status.DONE && countNew == 0) {
                countDone++;
            } else {
                break;
            }
        }

        if (countDone == epic.getSubtaskList().size()) {
            isDone = true;
        } else if (countNew == epic.getSubtaskList().size()) {
            isNew = true;
        }

        if (epic.getSubtaskList().isEmpty() || isNew) {
            epic.setStatus(Status.NEW);
        } else if (isDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
