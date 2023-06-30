package InterviewTask;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CustomBlockingQueueUsingLinkedList<T> {

    private Queue<T> queue = new LinkedList<>();
    private Lock lock = new ReentrantLock();

    private Condition putCondition = lock.newCondition();
    private Condition takeCondition = lock.newCondition();

    private int size;

    public CustomBlockingQueueUsingLinkedList(int size) {
        this.size = size;
    }

    private void put(T obj) {
        lock.lock();
        try {
            while (queue.size() >= size) {
                try {
                    putCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Putting data: " + obj);
            queue.add(obj);
            takeCondition.signalAll();
        }finally {
            lock.unlock();
        }
    }



    private T take() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    takeCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Taking data");
            var data = queue.poll();

            putCondition.signalAll();
            return data;
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        var queue = new CustomBlockingQueueUsingLinkedList<Integer>(5);

        var consumer = new Thread(() -> {
            int i = 0;
            while (i < 20) {
                System.out.println(queue.take());
                i++;
            }
        });

        var producer = new Thread(() -> {
            int i = 0;
            while (i < 20) {
                queue.put(i);

                i++;
            }
        });


        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }
}
