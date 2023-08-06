package executorServices;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutoThreadPoolTest {

    @Test
    void whenSingleScheduled_thenSuccess() {
        var runnable = getRunnable();
        var scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutor.scheduleAtFixedRate(runnable, 1000L, 100L, TimeUnit.MILLISECONDS);
        await();

    }


    @Test
    void whenSingleScheduled_thenSuccess2() {
        var runnable = getRunnable();
        var scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutor.scheduleWithFixedDelay(runnable, 100L, 100L, TimeUnit.MILLISECONDS);
        await();

    }

    @Test
    void name() {
        var pool = Executors.newScheduledThreadPool(10);
        System.out.println("main start time " + MyRunnable.now());

        for (int i = 0; i <3; i++) {

            var myRunnable = new MyRunnable("thread-" + i);
            System.out.println(myRunnable.getName() + "StartTime: " + MyRunnable.now());
            pool.schedule(myRunnable, 5, TimeUnit.SECONDS);
        }

 await();

        pool.shutdown();
        while (!pool.isTerminated()) {
            //wait for all threads to end
        }
        System.out.println("main method " + MyRunnable.now());
    }

    void await() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }



    static class MyRunnable implements Runnable {

        private String name;

        public MyRunnable(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println(getName()+" true start: "+ now());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(getName()+
                    " true end " + now());
        }

        static String now() {
            var simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.format(new Date());
        }
    }
    private static Runnable getRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                System.out.println("Perfomed in " +
                        Thread.currentThread().getName() + " on " + LocalDateTime.now());
            }
        };
    }
}
