package parsing;

import enums.Status;
import managers.FileBackedTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class TaskParser {
    public static TaskData parseTaskLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Строка не может быть пустой");
        }

        String[] parts = line.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Недостаточно данных в строке");
        }

        TaskData data = new TaskData();
        try {
            data.setId(Integer.parseInt(parts[0].trim()));
            data.setType(validateType(parts[1].trim()));
            data.setName(parts[2].trim());
            data.setStatus(Status.valueOf(parts[3].trim()));
            data.setDescription(parts[4].trim());

            if (parts.length > 5 && !parts[5].equals("null")) {
                data.setStartTime(LocalDateTime.parse(parts[5].trim()));
            }
            if (parts.length > 6 && !parts[6].equals("null")) {
                data.setDuration(Duration.parse(parts[6].trim()));
            }
            if (parts.length > 7 && !parts[7].isEmpty()) {
                data.setEpicId(Integer.parseInt(parts[7].trim()));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ошибка формата числа", e);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ошибка формата даты/времени", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ошибка формата данных", e);
        }

        return data;
    }

    public static void createAndAddTask(FileBackedTaskManager manager, TaskData data) {
        switch (data.getType()) {
            case "TASK":
                Task task = new Task(data.getName(), data.getDescription(), data.getId(),
                        data.getStatus(), data.getStartTime(), data.getDuration());
                manager.addTask(task);
                break;
            case "EPIC":
                Epic epic = new Epic(data.getName(), data.getDescription(),
                        data.getId(), data.getStatus());
                manager.addEpic(epic);
                break;
            case "SUBTASK":
                Subtask subtask = new Subtask(data.getName(), data.getDescription(), data.getId(),
                        data.getStatus(), data.getStartTime(), data.getDuration(), data.getEpicId());
                manager.addSubtask(subtask);
                break;
        }
    }

    private static String validateType(String type) {
        if (!type.equals("TASK") && !type.equals("EPIC") && !type.equals("SUBTASK")) {
            throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
        return type;
    }
}
