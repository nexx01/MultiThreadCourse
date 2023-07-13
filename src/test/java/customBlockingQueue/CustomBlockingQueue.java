package customBlockingQueue;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CustomBlockingQueue<T> {
    private final Lock lock = new ReentrantLock();
    private final Condition takeCondition = lock.newCondition();
    private final Condition putCondition = lock.newCondition();

    private final List<T> queue = new LinkedList<>();

    public   int count = 0;
    private final int size;

    public CustomBlockingQueue(int size) {
        this.size = size;
    }

    public T take() {
        try {
            lock.lock();
            while (count == 0) {
                takeCondition.await();
            }
            count--;
            var t = queue.get(count);
            queue.remove(count);
            putCondition.signalAll();
            return t;
        } catch (InterruptedException exception) {
exception.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }

    public void put(T obj) {
        try {
            lock.lock();
            while (count >= size) {
                putCondition.await();
            }
            count++;
            queue.add(obj);
            takeCondition.signalAll();
        } catch (InterruptedException exception) {
exception.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

class Test {
    @org.junit.jupiter.api.Test
    void name() throws InterruptedException {
        var qu = new CustomBlockingQueue<Integer>(20);
        var thread1 = new Thread(() -> {
            System.out.println("thread1");
            for (int i = 0; i < 100; i++) {
                System.out.println("put , size " + qu.count);
                qu.put(i);

            };
        });


        var thread2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("take , size " + qu.count);
                var take = qu.take();
                System.out.println("take value: "+ take);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });



        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

       // Thread.sleep(90000);
    }
}
