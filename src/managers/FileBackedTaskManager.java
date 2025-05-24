package managers;

import enums.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import exeptions.ManagerSaveException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic updatedEpic = super.updateEpic(epic);
        save();
        return updatedEpic;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());

            //Пропускаем заголовок и обрабатываем каждую строку
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String type = parts[1].trim();
                    String name = parts[2].trim();
                    String status = parts[3].trim();
                    String description = parts[4].trim();
                    LocalDateTime startTime = null;
                    Duration duration = null;
                    if (parts.length > 5 && !parts[5].equals("null")) { //Обрабатываем null-случай
                        startTime = LocalDateTime.parse(parts[5].trim());
                    }
                    if (parts.length > 6 && !parts[6].equals("null")) {
                        duration = Duration.parse(parts[6].trim());
                    }
                    int epicId = -1; //Устанавливаем значение по умолчанию

                    //Проверяем, есть ли epicId
                    if (parts.length > 7 && !parts[7].isEmpty()) {
                        epicId = Integer.parseInt(parts[7].trim());
                    }

                    Status taskStatus = Status.valueOf(status);

                    //Создаем задачи в зависимости от типа
                    switch (type) {
                        case "TASK":
                            Task task = new Task(name, description, id, taskStatus,
                                    startTime, duration);
                            manager.addTask(task);
                            break;
                        case "EPIC":
                            Epic epic = new Epic(name, description, id, taskStatus);
                            manager.addEpic(epic);
                            break;
                        case "SUBTASK":
                            Subtask subtask = new Subtask(name, description, id,
                                    taskStatus, startTime, duration, epicId);
                            manager.addSubtask(subtask);
                            break;
                    }
                } catch (NumberFormatException e) {
                    throw new ManagerSaveException("Ошибка формата числа в данных", e);
                } catch (IllegalArgumentException e) {
                    throw new ManagerSaveException("Ошибка формата статуса в данных", e);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке данных", e);
        }
        return manager;
    }

    private void save() {
        StringBuilder sb = new StringBuilder();
        sb.append("id,type,name,status,description,startTime,duration,epic\n");

        getTaskList().forEach(task -> sb.append(task.toCsvString()));
        getEpicList().forEach(epic -> sb.append(epic.toCsvString()));
        getSubtasksList().forEach(subtask -> sb.append(subtask.toCsvString()));

        try {
            Files.writeString(file.toPath(), sb.toString(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных", e);
        }
    }
}
