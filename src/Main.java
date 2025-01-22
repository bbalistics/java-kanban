public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        //Создаем таски, сабтаски, эпики, добавляем их в менеджер, выводим.
        Task task1 = new Task("Помыть  пол", "Использовать швабру");
        Task task2 = new Task("Протереть полки", "Используй новую тряпку");

        manager.addTask(task1);
        manager.addTask(task2);

        Epic epic1 = new Epic("Выкинуть мусор", "Давно пора");
        Epic epic2 = new Epic("Почисти картошку","Как можно скорее");

        manager.addEpic(epic1);
        manager.addEpic(epic2);

        Subtask subtask1 = new Subtask("Найди пакет", "Он лежит в комнате 1",epic1.getId());
        Subtask subtask2 = new Subtask("Собери мусор, и выкинь его", "Используй пакет", epic1.getId());
        Subtask subtask3 = new Subtask("Найди нож, почисти картошку", "Быстрее", epic2.getId());

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        System.out.println(manager.getTaskList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubtasksList());
        System.out.println("________________________________");

        //Обновляем статусы тасков, сабтасков, выводим информацию на проверку.
        Task updatedTask1 = new Task(task1.getName(), task1.getDescription(), task1.getId(), Status.DONE);
        Task updatedTask2 = new Task(task2.getName(), task2.getDescription(), task2.getId(), Status.IN_PROGRESS);

        manager.updateTask(updatedTask1);
        manager.updateTask(updatedTask2);

        Subtask updatedSubtask1 = new Subtask(subtask1.getName(), subtask1.getDescription(), subtask1.getId(), Status.IN_PROGRESS, subtask1.getEpicId());
        Subtask updatedSubtask2 = new Subtask(subtask2.getName(), subtask2.getDescription(), subtask2.getId(), Status.DONE, subtask2.getEpicId());
        Subtask updatedSubtask3 = new Subtask(subtask3.getName(), subtask3.getDescription(), subtask3.getId(), Status.DONE, subtask3.getEpicId());

        manager.updateSubtask(updatedSubtask1);
        manager.updateSubtask(updatedSubtask2);
        manager.updateSubtask(updatedSubtask3);

        System.out.println(manager.getTaskList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubtasksList());
        System.out.println("_________________________");

        //Удаляем один таск, один эпик, проверяем информацию.
        manager.deleteTaskById(1);
        manager.deleteEpicById(3);

        System.out.println(manager.getTaskList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubtasksList());
    }
}