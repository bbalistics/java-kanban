package managers;

import parsing.TaskData;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import exeptions.ManagerSaveException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
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

            // Пропускаем заголовок и обрабатываем каждую строку
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                try {
                    TaskData parsedData = TaskData.parseTaskLine(line);
                    TaskData.createAndAddTask(manager, parsedData);
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
