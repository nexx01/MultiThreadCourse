package InterviewTask;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CustomBlockingQueue {

    private Lock lock = new ReentrantLock();
    private Condition putCondition = lock.newCondition();
    private Condition takeCondition = lock.newCondition();
    private Object[] queue;

    private int putIndex;
    private int takeIndex;
    private int count;

    private int queueSize;

    public CustomBlockingQueue(int queueSize) {
        this.queueSize = queueSize;
        queue = new Object[queueSize];
    }

    public void put(Object data) {
        lock.lock();
        try{
            while (count >= queueSize) {
                try {
                    putCondition.await();
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
            System.out.println("Queuing value :" + data);
            queue[putIndex] = data;
            count++;
        }
        finally {
            lock.unlock();
        }
    }

    public Object take() {
        lock.lock();
        try {
            while (count == 0) {
                try {
                    takeCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            var data = queue[takeIndex];
            count--;

            if (++takeIndex >= queueSize) {
                takeIndex = 0;
            }
            putCondition.signalAll();
            return data;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        var customBlockingQueue = new CustomBlockingQueue(5);

        var consumerThread = new Thread(() -> {
            int i = 0;
            while (i < 10) {
                System.out.println("data: " + customBlockingQueue.take());
                i++;
            }
        }, "ConsumerThread");

        var producerThread = new Thread(() -> {
            int i = 0;
            while (i < 10) {
                customBlockingQueue.put(i);
                i++;
            }
        }, "Producer thread");

        producerThread.join();
        consumerThread.join();

        producerThread.start();
        consumerThread.start();
    }

}
