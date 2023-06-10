import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleTest {

    /*
    *  Читающий поток только один раз поймал изменение переменной
    *
    * reader:value = 1
writer: changed to 1
writer: changed to 2
writer: changed to 3
writer: changed to 4
writer: changed to 5
    */
    @Test
    void simpleCounterTest() throws InterruptedException {
        var simpleCounter = new SimpleCounter();

        var reader = new Thread(() -> {
            long temp = 0;
            while (true) {
                var current = simpleCounter.get();
                if (temp != current) {
                    temp = current;
                    System.out.println("reader:value = " + current);
                }
            }
        });

        var writer = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                simpleCounter.increment();
                System.out.println("writer: changed to " + simpleCounter.get());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        reader.start();
        writer.start();

        reader.join();
        writer.join();
    }

    // step 1 testing in one thread
    @ParameterizedTest
    @ValueSource(ints = {0,1,2,500,Integer.MAX_VALUE})
    void shouldReturnCorrectCount(int number) {
        var simpleCounter = new SimpleCounter();
        for (int i = 0; i < number; i++) {
            simpleCounter.increment();
        }

        assertEquals(number, simpleCounter.get());
    }

    // step 1 testing in one thread
    @ParameterizedTest
    @ValueSource(ints = {0,1,2,500,Integer.MAX_VALUE})
    void shouldReturnCorrectCount2(int number) {
        var simpleCounter = new CorrectCounter();
        for (int i = 0; i < number; i++) {
            simpleCounter.increment();
        }

        assertEquals(number, simpleCounter.get());
    }


    //тест не отлавливает ошибку((((
    @Test
    void testWithManyThreads() throws InterruptedException {
        var counter = new SimpleCounter();
        var countDownLatch = new CountDownLatch(1);
        int number = 1000;
        for (int i = 0; i < number; i++) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                counter.increment();
            }).start();
        }
        countDownLatch.countDown();
        Thread.sleep(1000);
        assertEquals(number,counter.get());
    }

    @Test
    void testWithManyThreads2() throws InterruptedException {
        var counter = new CorrectCounter();
        var countDownLatch = new CountDownLatch(1);
        int number = 1000;
        for (int i = 0; i < number; i++) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                counter.increment();
            }).start();
        }
        countDownLatch.countDown();
        Thread.sleep(1000);
        assertEquals(number,counter.get());
    }
}


class SimpleCounter {
    long count;

    public void increment() {
        count++;
    }

    public long get() {
        return count;
    }
}

class CorrectCounter {
    private AtomicLong count= new AtomicLong();



    public void increment() {
        count.incrementAndGet();
    }

    public long get() {
        return count.get();
    }
}

