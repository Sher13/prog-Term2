package queue;

public class ArrayQueueTest {
    static int z = 0;
    public static void fill(ArrayQueue queue, int x) {
        for (int i = 0; i < x; i++) {
            queue.enqueue(z);
            z++;
        }
    }
    public static void dele(ArrayQueue queue, int i) {
        for (int j = 0; j < i; j++) {
            System.out.println(queue.size() + " " +
                    queue.element() + " " + queue.dequeue());
        }
    }

    public static void dump(ArrayQueue queue) {
        while (!queue.isEmpty()) {
            System.out.println(queue.size() + " " +
                    queue.element() + " " + queue.dequeue());
        }
    }

    public static void main(String[] args) {
       /* ArrayQueue queue = new ArrayQueue();

        fill(queue, 3);
        dele(queue, 3);
        queue.push(20);
        queue.push(21);
        fill(queue, 6);
        dele(queue, 2);
        fill(queue, 7);
        dmp(queue);
        fill(queue, 3);
        fill(queue, 4);
        dmp(queue); */

        LinkedQueue q = new LinkedQueue();
        q.enqueue(3);
        q.enqueue(5);
        System.out.println(q.dequeue());
        System.out.println(q.dequeue());
    }
}

