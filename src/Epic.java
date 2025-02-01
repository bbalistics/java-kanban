import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskListId = new ArrayList<>();

    Epic(String name, String description, int id, Status status) {
        super(name, description, id, status);
    }

    Epic(String name, String description) {
        super(name, description);
    }

    public void addSubtaskId (Subtask subtask) {
        subtaskListId.add(subtask.getId());
    }

    public void deleteSubtaskId (Integer id) {
        subtaskListId.remove(id);
    }

    public void deleteAllSubtaskId () {
        subtaskListId.clear();
    }

    public ArrayList<Integer> getSubtaskListId() {
        return subtaskListId;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name= " + getName() + '\'' +
                ", description = " + getDescription() + '\'' +
                ", id=" + getId() +
                ", subtaskList.size = " + subtaskListId.size() +
                ", status = " + getStatus() +
                '}';
    }
}
