package parsing;

import enums.Status;
import managers.FileBackedTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskData {
    private int id;
    private String type;
    private String name;
    private Status status;
    private String description;
    private LocalDateTime startTime;
    private Duration duration;
    private int epicId;

    public static TaskData parseTaskLine(String line) {
        String[] parts = line.split(",");

        TaskData data = new TaskData();
        data.id = Integer.parseInt(parts[0].trim());
        data.type = parts[1].trim();
        data.name = parts[2].trim();
        data.status = Status.valueOf(parts[3].trim());
        data.description = parts[4].trim();

        if (parts.length > 5 && !parts[5].equals("null")) { //Обрабатываем null-случай
            data.startTime = LocalDateTime.parse(parts[5].trim());
        }

        if (parts.length > 6 && !parts[6].equals("null")) {
            data.duration = Duration.parse(parts[6].trim());
        }

        if (parts.length > 7 && !parts[7].isEmpty()) {
            data.epicId = Integer.parseInt(parts[7].trim());
        }

        return data;
    }

    //Метод для создания и добавления задачи
    public static void createAndAddTask(FileBackedTaskManager manager, TaskData data) {
        switch (data.type) {
            case "TASK":
                Task task = new Task(data.name, data.description, data.id, data.status,
                        data.startTime, data.duration);
                manager.addTask(task);
                break;
            case "EPIC":
                Epic epic = new Epic(data.name, data.description, data.id, data.status);
                manager.addEpic(epic);
                break;
            case "SUBTASK":
                Subtask subtask = new Subtask(data.name, data.description, data.id,
                        data.status, data.startTime, data.duration, data.epicId);
                manager.addSubtask(subtask);
                break;
        }
    }
}
