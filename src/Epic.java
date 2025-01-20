import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskListId = new ArrayList<>();

    Epic(String name, String description) {
        super(name, description);
    }

    public void addSubtaskId (Subtask subtask) {
        subtaskListId.add(subtask.getId());
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
