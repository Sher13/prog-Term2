package queue;

import java.util.Arrays;

public class ArrayQueueModule {
    private static int size;
    private static int nw;
    private static Object[] elements = new Object[5];

    public static void enqueue(Object element) {
        assert element != null;

        ensureCapacity(size+1);
        elements[(size+nw)%elements.length] = element;
        size++;
    }
    public static void push(Object element) {
        assert element != null;

        ensureCapacity(size+1);
        nw = (nw-1+elements.length)%elements.length;
        elements[nw] = element;
        size++;
    }

    private static void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            elements = Arrays.copyOf(elements, 2*capacity);
            System.arraycopy(elements, 0, elements, size, size);
            System.arraycopy(elements, nw, elements, 0, size-nw);
            System.arraycopy(elements, size, elements, size-nw, nw);
            nw = 0;
        }
    }

    public static Object dequeue() {
        assert size > 0;

        size--;
        Object val = elements[nw];
        nw = (nw+1)%elements.length;
        return val;
    }

    public static Object remove() {
        assert size > 0;

        size--;
        return elements[(nw+size)%elements.length];
    }

    public static Object element() {
        assert size > 0;

        return elements[nw];
    }

    public static Object peek() {
        assert size > 0;

        return elements[(nw+size-1)%elements.length];
    }

    public static int size() {
        return size;
    }

    public static boolean isEmpty() {
        return size == 0;
    }
    public static void clear() {
        size = 0;
        nw = 0;
        elements = Arrays.copyOf(elements, 5);
    }
}
