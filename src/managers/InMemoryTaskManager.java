package managers;

import enums.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private int id = 1;
    private HistoryManager historyManager = Managers.getDefaultHistory();
    private final Set<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));

    @Override
    public List<Task> getTaskList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpicList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
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
        epics.values().forEach(epic -> {
            epic.deleteAllSubtaskId();
            epic.setStatus(Status.NEW);
        });
    }

    @Override
    public void addTask(Task task) {
        if (isTaskOverlappingWithExisting(task)) {
            throw new IllegalStateException("Задача пересекается по времени с существующей");
        }
        task.setId(generateId());
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (isTaskOverlappingWithExisting(subtask)) {
            throw new IllegalStateException("Подзадача пересекается по времени с существующей");
        }
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtaskId(subtask);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        checkEpicStatus(epic);
        epic.updateTime(getEpicsSubtasks(epic));
    }

    @Override
    public void updateTask(Task task) {
        if (isTaskOverlappingWithExisting(task)) {
            throw new IllegalStateException("Задача пересекается по времени с существующей");
        }
        Task oldTask = tasks.get(task.getId());
        if (oldTask != null && oldTask.getStartTime() != null) {
            prioritizedTasks.remove(oldTask);
        }
        tasks.replace(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic oldEpic = epics.get(epic.getId());
        oldEpic.getSubtaskListId().stream()
                .filter(subtaskId -> !epic.getSubtaskListId().contains(subtaskId))
                .forEach(subtasks::remove);

        epics.replace(epic.getId(), epic);
        checkEpicStatus(epic);
        epic.updateTime(getEpicsSubtasks(epic));
        return epic;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (isTaskOverlappingWithExisting(subtask)) {
            throw new IllegalStateException("Подзадача пересекается по времени с существующей");
        }
        Subtask oldSubtask = subtasks.get(subtask.getId());
        if (oldSubtask != null && oldSubtask.getStartTime() != null) {
            prioritizedTasks.remove(oldSubtask);
        }
        subtasks.replace(subtask.getId(), subtask);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        Epic epic = epics.get(subtask.getEpicId());
        checkEpicStatus(epic);
        epic.updateTime(getEpicsSubtasks(epic));
    }

    @Override
    public void deleteTaskById(int id) {
        Task task = tasks.remove(id);
        if (task != null && task.getStartTime() != null) {
            prioritizedTasks.remove(task);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        Optional.ofNullable(epics.remove(id))
                .map(Epic::getSubtaskListId)
                .ifPresent(subtaskIds -> subtaskIds.forEach(subtasks::remove));
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            if (subtask.getStartTime() != null) {
                prioritizedTasks.remove(subtask);
            }
            Epic epic = epics.get(subtask.getEpicId());
            epic.deleteSubtaskId(id);
            checkEpicStatus(epic);
            epic.updateTime(getEpicsSubtasks(epic));
        }
    }

    @Override
    public List<Subtask> getEpicsSubtasks(Epic epic) {
        return epic.getSubtaskListId().stream()
                .map(subtasks::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public boolean isTasksOverlap(Task task1, Task task2) {
        if (task1 == null || task2 == null) {
            return false;
        }
        if (task1.getStartTime() == null || task1.getEndTime() == null ||
                task2.getStartTime() == null || task2.getEndTime() == null) {
            return false;
        }
        return !task1.getEndTime().isBefore(task2.getStartTime()) &&
                !task1.getStartTime().isAfter(task2.getEndTime());
    }

    public boolean isTaskOverlappingWithExisting(Task newTask) {
        if (newTask.getStartTime() == null || newTask.getEndTime() == null) {
            return false;
        }

        return getAllTasksWithTime().stream()
                .filter(existingTask -> existingTask.getId() != newTask.getId()) //Исключаем саму задачу
                .filter(existingTask -> !(existingTask instanceof Epic)) //Исключаем все Epic
                .anyMatch(existingTask -> isTasksOverlap(newTask, existingTask));
    }

    private List<Task> getAllTasksWithTime() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values().stream()
                .filter(task -> task.getStartTime() != null)
                .toList());
        allTasks.addAll(subtasks.values().stream()
                .filter(task -> task.getStartTime() != null)
                .toList());
        return allTasks;
    }

    private int generateId() {
        return id++;
    }

    private void checkEpicStatus(Epic epic) {
        if (subtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        List<Status> statuses = epic.getSubtaskListId().stream()
                .map(subtasks::get)
                .filter(Objects::nonNull)
                .map(Subtask::getStatus)
                .toList();

        boolean allNew = statuses.stream().allMatch(s -> s == Status.NEW);
        boolean allDone = statuses.stream().allMatch(s -> s == Status.DONE);

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
