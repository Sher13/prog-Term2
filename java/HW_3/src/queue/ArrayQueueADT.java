package queue;

import java.util.Arrays;

public class ArrayQueueADT {
    private /*static*/ int size;
    private /*static*/ int nw;
    private /*static*/ Object[] elements = new Object[10];

    public static void enqueue(ArrayQueueADT queue, Object element) {
        assert element != null;

        ensureCapacity(queue, queue.size+1);
        queue.elements[(queue.size+queue.nw)%queue.elements.length] = element;
        queue.size++;
    }

    public static void push(ArrayQueueADT queue, Object element) {
        assert element != null;

        ensureCapacity(queue,queue.size+1);
        queue.elements[(queue.nw-1+queue.elements.length)%queue.elements.length] = element;
        queue.nw = (queue.nw-1+queue.elements.length)%queue.elements.length;
        queue.size++;
    }

    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        if (capacity > queue.elements.length) {
            queue.elements = Arrays.copyOf(queue.elements, 2*capacity);
            System.arraycopy(queue.elements, 0, queue.elements, queue.size, queue.size);
            System.arraycopy(queue.elements, queue.nw, queue.elements, 0, queue.size-queue.nw);
            System.arraycopy(queue.elements, queue.size, queue.elements, queue.size-queue.nw, queue.nw);
            queue.nw = 0;
        }
    }

    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.size > 0;

        queue.size--;
        Object val = queue.elements[queue.nw];
        queue.nw = (queue.nw+1)%queue.elements.length;
        return val;
    }

    public static Object remove(ArrayQueueADT queue) {
        assert queue.size > 0;

        queue.size--;
        return queue.elements[(queue.nw+queue.size)%queue.elements.length];
    }

    public static Object element(ArrayQueueADT queue) {
        assert queue.size > 0;

        return queue.elements[queue.nw];
    }

    public static Object peek(ArrayQueueADT queue) {
        assert queue.size > 0;

        return queue.elements[(queue.nw+queue.size-1)%queue.elements.length];
    }

    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }
    public static void clear(ArrayQueueADT queue) {
        queue.size = 0;
        queue.nw = 0;
        queue.elements = Arrays.copyOf(queue.elements, 5);
    }
}

