package tasks;

import enums.Status;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String name, String description, int id, Status status, int epicID) {
        super(name, description, id, status);
        this.epicId = epicID;
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "tasks.Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", epicId=" + epicId +
                ", status=" + getStatus() +
                '}';
    }
}
