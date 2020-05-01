package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue{
    protected int size;
    public void enqueue(Object element) {
        assert element != null;

        enqueueImpl(element);
        size++;
    }
    protected abstract void enqueueImpl(Object element);

    public Object dequeue() {
        assert size > 0;

        size--;
        return dequeueImpl();
    }
    protected abstract Object dequeueImpl();

    public Object element() {
        assert size > 0;

        return elementImpl();
    }
    protected abstract Object elementImpl();

    public void clear() {
        size = 0;
        clearImpl();
    }
    protected abstract void clearImpl();

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Queue filter(Predicate<Object> pr) {
        Queue q1 = create();
        int sz = size;
        for (int i = 0; i < size;i++) {
            Object nw = this.dequeue();
            if (pr.test(nw))
                q1.enqueue(nw);
            this.enqueue(nw);
        }
        return q1;
    }

    protected abstract Queue create();

    public Queue map(Function<Object, Object> f) {
        Queue q1 = create();
        int sz = size;
        for (int i = 0; i < size;i++) {
            Object nw = this.dequeue();
            q1.enqueue(f.apply(nw));
            this.enqueue(nw);
        }
        return q1;
    }
}
