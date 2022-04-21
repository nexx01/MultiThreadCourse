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
}
