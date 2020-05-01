package queue;

public class LinkedQueueModule {
    private static Node head;
    private static Node tail;
    private static int size;

    public static void enqueue(Object element) {
        assert element != null;

        size++;
        tail = new Node(element, tail, null);
        if (size == 1)
            head = tail;
    }

    public static void push(Object element) {
        assert  element != null;

        size++;
        head = new Node(element, null, head);
        if (size == 1)
            tail = head;
    }

    public static Object dequeue() {
        assert size > 0;

        size--;
        Object result = head.value;
        head = head.next;
        return result;
    }

    public static Object remove() {
        assert  size > 0;

        size--;
        Object result = tail.value;
        tail = tail.prev;
        return result;
    }

    public static Object element() {
        assert size > 0;

        return head.value;
    }

    public static Object peek() {
        assert size > 0;

        return tail.value;
    }

    public static int size() {
        return size;
    }

    public static boolean isEmpty() {
        return size == 0;
    }

    private static class Node {
        private Object value;
        private Node next;
        private Node prev;

        public Node(Object value, Node prev, Node next) {
            assert value != null;

            this.value = value;
            this.next = next;
            this.prev = prev;
            if (prev != null)
                prev.next = this;
            if (next != null)
                next.prev = this;
        }
    }
}
