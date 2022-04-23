import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolExecutorsTest {
    @Test
    void name() {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        executorService.setMaximumPoolSize(1);
        executorService.setCorePoolSize(1);

        Runnable task = () -> {
            System.out.println("----------------------");
        };

        executorService.submit(task);
        //executorService.submit(task);
    }
    class Task implements Runnable{
        private final ThreadLocal<Integer> value =
                ThreadLocal.withInitial(() -> 0);

        @Override
        public void run() {
            Integer integer = value.get();
            value.set(integer + 1);
            System.out.println(value.get());
            value.remove();
        }
    }

    @Test
    void name2() {

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            fixedThreadPool.submit(new Task());
        }


    }
}
