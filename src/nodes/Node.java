package nodes;

public class Node<T> {
    public T data;
    public Node<T> next;
    public Node<T> prev;

    public Node(T data, Node<T> next, Node<T> prev) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}

/**
 * Node является узлом связного списка, содержит в себе поле data (данные внутри элемента), две ссылки на следующий и
 * предыдущий элемент (next и prev соответственно).
 */