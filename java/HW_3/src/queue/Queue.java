package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public interface Queue {
    // Inv: size >= 0 && \forall i = [1, size] : a_i != null

    // Pre: element != null
    // Post: size = size' + 1 && a[size'] = element
    void enqueue(Object element);

    // Pre: size > 0
    // Post: size = size' - 1 && \forall i : a[i] = a[i+1]
    Object dequeue();

    // Pre: size > 0
    // Post: Immutable ret a[0]
    Object element();

    // Pre: -
    // Post: Immutable
    int size();

    // Pre: -
    // Post: Immutable
    boolean isEmpty();

    // Pre: -
    // Post: size = 0
    void clear();

    // Pre: -
    // Post: return Queue with element :  pr(a[i]) = true && Immutable
    Queue filter(Predicate<Object> pr);

    // Pre: -
    // Post: return Queue with all element, where a[i'] = f(a[i]) && Immutable
    Queue map(Function<Object, Object> f);
}
