package tasks;

import enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private ArrayList<Integer> subtaskListId = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description, int id, Status status) {
        super(name, description, id, status, null, null);
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public void addSubtaskId(Subtask subtask) {
        subtaskListId.add(subtask.getId());
    }

    public void deleteSubtaskId(Integer id) {
        subtaskListId.remove(id);
    }

    public void deleteAllSubtaskId() {
        subtaskListId.clear();
    }

    public ArrayList<Integer> getSubtaskListId() {
        return subtaskListId;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void updateTime(List<Subtask> allSubtasks) {
        if (allSubtasks == null || allSubtasks.isEmpty() || subtaskListId.isEmpty()) {
            resetTime();
            return;
        }

        LocalDateTime earliestStart = null;
        LocalDateTime latestEnd = null;
        long totalDuration = 0;

        for (Subtask subtask : allSubtasks) {
            if (subtaskListId.contains(subtask.getId())) {
                //Обновляем самое раннее время начала
                if (subtask.getStartTime() != null) {
                    if (earliestStart == null || subtask.getStartTime().isBefore(earliestStart)) {
                        earliestStart = subtask.getStartTime();
                    }
                }

                //Обновляем самое позднее время окончания
                LocalDateTime subtaskEnd = subtask.getEndTime();
                if (subtaskEnd != null) {
                    if (latestEnd == null || subtaskEnd.isAfter(latestEnd)) {
                        latestEnd = subtaskEnd;
                    }
                }

                //Суммируем продолжительность
                if (subtask.getDuration() != null) {
                    totalDuration += subtask.getDuration().toMinutes();
                }
            }
        }

        setStartTime(earliestStart);
        setDuration(totalDuration > 0 ? Duration.ofMinutes(totalDuration) : null);
        this.endTime = latestEnd;
    }

    private void resetTime() {
        setStartTime(null);
        setDuration(null);
        this.endTime = null;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", status=" + getStatus() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                ", endTime=" + endTime +
                ", subtaskIds=" + subtaskListId +
                '}';
    }
}

