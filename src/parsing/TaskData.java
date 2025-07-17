package parsing;

import java.time.Duration;
import java.time.LocalDateTime;
import enums.Status;

public class TaskData {
    private int id;
    private String type;
    private String name;
    private Status status;
    private String description;
    private LocalDateTime startTime;
    private Duration duration;
    private int epicId = -1;

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
