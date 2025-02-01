public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task = new Task("Помыть посуду", "Использовать новое средство");
        Epic epic = new Epic("Уберись дома", "Используй пылесос");
        Subtask subtask = new Subtask("Найди пылесос", "Поищи в кладовке", 2);
        Subtask subtask2 = new Subtask("Пропылесось дом", "Скорее", 2);

        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
        manager.addSubtask(subtask2);

        manager.getEpicById(2);
        manager.getSubtaskById(3);

        printAllTasks(manager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTaskList()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpicList()) {
            System.out.println(epic);

            for (Task task : manager.getEpicsSubtasks((Epic) epic)) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasksList()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}