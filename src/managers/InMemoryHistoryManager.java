package managers;

import nodes.Node;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Node<Task>> historyList = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    /**
     * linkLast добавляет задачу в конец списка historyList:
     * дублирует ссылку текущего последнего элемента (tail) в переменную oldTail, добавляет новый узел в конец списка,
     * затем если head пуст, добавляет в него новый узел, в противном случае добавляет старому "хвосту" ссылку next на
     * новый "хвост".
     */

    public void linkLast(Task task) {
        Node<Task> oldTail = tail;
        Node<Task> newNode = new Node(task, null, oldTail);
        tail = newNode;
        historyList.put(task.getId(), newNode);

        if (head == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
    }

    @Override
    public void addTask(Task task) {
        remove(task.getId());
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        removeNode(historyList.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> node = head;

        while (node != null) {
            tasks.add(node.data);
            node = node.next;
        }

        return tasks;
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            Node<Task> prev = node.prev;
            Node<Task> next = node.next;
            node.data = null;

            if ((node == head) && (node == tail)) {
                head = null;
                tail = null;
            } else if ((node != head) && (node == tail)) {
                tail = prev;
                tail.next = null;
            } else if ((node == head) && (node != tail)) {
                head = next;
                head.prev = null;
            } else {
                prev.next = next;
                next.prev = prev;
            }
        }
    }
}
