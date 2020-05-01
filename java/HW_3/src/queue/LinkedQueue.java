package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public class LinkedQueue extends AbstractQueue  {
    private Node head;
    private Node tail;

    public Queue create() {
        return new LinkedQueue();
    }
    public void enqueueImpl(Object element) {
        tail = new Node(element, tail, null);
        if (size == 0)
            head = tail;
    }

    public Object dequeueImpl() {
        Object result = head.value;
        head = head.next;
        return result;
    }

    public Object elementImpl() {
        return head.value;
    }


    public void clearImpl() {
        tail = null;
        head = null;
    }

    private class Node {
        private Object value;
        private Node next;

        public Node(Object value,Node prev, Node next) {
            assert value != null;

            this.value = value;
            this.next = next;
            if (prev != null)
                prev.next = this;
        }
    }
}
