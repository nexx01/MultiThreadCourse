package InterviewTask;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CustomBlockingQueue2 {

    private Lock lock = new ReentrantLock();
    private Condition putCondition = lock.newCondition();
    private Condition takeCondition = lock.newCondition();
    private Object[] queue;
    private int queuesize;

    private int takeIndex;
    private int putIndex;
    private int count;


    public CustomBlockingQueue2(int queuesize) {
        this.queuesize = queuesize;
        queue = new Object[queuesize];
    }

    public void put(Object data) {
        lock.lock();
        try {
            while (count >= queuesize) {
                try {
                    putCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Queuing value :" + data);
            queue[putIndex] = data;
            count++;
            takeCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public Object take() {
        lock.lock();
        try {
            while (count ==0) {
                try {
                    takeCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            var data = queue[takeIndex];
            count--;

            if (++takeIndex >= queuesize) {
                takeIndex = 0;
            }
            putCondition.signalAll();
            return data;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        var customBlockingQueue = new CustomBlockingQueue2(5);

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

        consumerThread.start();
        producerThread.start();
    }

}
