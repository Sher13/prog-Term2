package queue;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public class ArrayQueue extends AbstractQueue{
    // Inv: size >= 0 && \forall i = [1, size] : a_i != null
    private int nw;
    private Object[] elements = new Object[5];

    public Queue create() {
        return new ArrayQueue();
    }
    // element != null
    public void enqueueImpl(Object element) {
        ensureCapacity(size+1);
        elements[(size+nw)%elements.length] = element;
    }
    // size = size' + 1 && a[size'] = element

    private void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            elements = Arrays.copyOf(elements, 2*capacity);
            System.arraycopy(elements, 0, elements, size, size);
            System.arraycopy(elements, nw, elements, 0, size-nw);
            System.arraycopy(elements, size, elements, size-nw, nw);
            nw = 0;
        }
    }

    // size > 0
    public Object dequeueImpl() {
        Object val = elements[nw];
        nw = (nw+1)%elements.length;
        return val; // return a[0]
    }
    // size = size' - 1 && \forall i : a[i] = a[i+1]

    // size > 0
    public Object elementImpl() {
        return elements[nw]; // return a[0]
    }

    public void clearImpl() {
        nw = 0;
        elements = new Object[5];
    }
    // size = 0
}

