package interviewTask;

import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CustomSchedulersImplTest {

    @Test
    void name() throws InterruptedException {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("!");
            }
        },1L,1000L);

        Thread.sleep(10000);
    }

    @Test
    void name2() throws InterruptedException {
        fun(()-> System.out.println("22"),444L);
        Thread.sleep(10000);

    }

    void fun(Runnable runnable, long delay) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        }, delay);
    }

    private ScheduledExecutorService executorService;
    @Test
    void when_scheduledExecutor() throws InterruptedException {
         executorService= Executors.newSingleThreadScheduledExecutor();

        ScheduledFuture<?> solution = solution(() -> System.out.println("Hello world!"), 11);
        Thread.sleep(10000);

        executorService.shutdown();
        executorService.shutdownNow();
        executorService = null;


    }

    public  ScheduledFuture<?> solution(Runnable command, int delay) {
        return executorService.schedule(command, delay, TimeUnit.MILLISECONDS);
    }

    @Test
    void givenUsingTimer_whenSchedulingTaskOnce_thenCorrect() {
        var timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Task perfomed on: "+ LocalDate.now() +
                        "\n Thread's name: "+Thread.currentThread().getName());
            }
        };

        var timer = new Timer();

        long delay = 1000L;
        timer.schedule(timerTask, delay);
        await();

    }


    @Test
    void givenUsingTimer_whenSchedulingTaskOnDate_thenCorrect() {
        var oldDatabase = List.of("Harrison Ford", "Carrie Fisher", "Mark Hamill");
        var newDatabase = new ArrayList<String>();

        var twoSecondsLater = LocalDateTime.now().plusSeconds(2);
        var twoSecondLatnerAsDate = Date.from(twoSecondsLater.atZone(ZoneId.systemDefault()).toInstant());

        var databaseMigrationTask = new DatabaseMigrationTask(oldDatabase, newDatabase);
        new Timer().schedule(databaseMigrationTask

                , twoSecondLatnerAsDate);


        System.out.println(databaseMigrationTask.newDatabase);
        await();
        System.out.println(databaseMigrationTask.newDatabase);

    }

    void await() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }

    }

    @Test
    void name3() {
        new Timer().schedule(new NewsletterTask(), 0, 1000);

        await();
    }


    @Test
    void whenScheduleAtFixedRate() {
        new Timer().scheduleAtFixedRate(new NewsletterTask(), 0, 1000);
        await();
    }

    @Test
    void givenUsingTimer_whenSchedulingDailyTask_thenCorrect() {

        var timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Task performed on " + new Date());
            }
        };

        var timer = new Timer("timer");
        long delay = 1000L;
        long period = 1000L * 60L * 60L * 24L;
        timer.scheduleAtFixedRate(timerTask, delay, period);

        await();
    }

    @Test
    void givenUsingTimer_whenCancelingTimerTask_thenCorrect() {
        var timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Task performed on " + new Date());
                cancel();
            }
        };

        var timer = new Timer("Timer");
        timer.scheduleAtFixedRate(timerTask, 1000L, 1000L);

        await();
    }

    @Test
    void givenUsingTimer_whenCancelingTimer_thenCorrect() {

        var timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Task perfomed on " + new Date());
            }
        };

        var timer = new Timer("Timer");
        timer.scheduleAtFixedRate(timerTask, 1000L, 1000L);

        await();
        timer.cancel();
        await();

    }

    @Test
    void givenUsingTimer_whenStoppingThread_thenTimerTaskIsCancelled() {

        var timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Task perfomed on " + new Date());
                Thread.currentThread().stop();

            }
        };

        var timer = new Timer("Timer");
        timer.scheduleAtFixedRate(timerTask, 1000L, 1000L);
        await();
    }

    @Test
    void givenExecutorService_whenSchedulingRepeatedTask_thenCorrect() {

        var timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Task perfomed on " + new Date());
            }
        };

        var scheduledExecutorService = Executors
                .newSingleThreadScheduledExecutor();
        long delay = 1000L;
        long period = 1000L;
        scheduledExecutorService.scheduleAtFixedRate(timerTask, delay, period, TimeUnit.MILLISECONDS);

        await();
        scheduledExecutorService.shutdown();
        scheduledExecutorService.shutdownNow();
    }
}

class DatabaseMigrationTask extends TimerTask {
     List<String> oldDatabase;
     List<String> newDatabase;

    public DatabaseMigrationTask(List<String> oldDatabase,
                                 List<String> newDatabase) {
        this.oldDatabase = oldDatabase;
        this.newDatabase = newDatabase;
    }

    @Override
    public void run() {
        newDatabase.addAll(oldDatabase);
    }



}




  class NewsletterTask extends TimerTask {


    @Override
    public void run() {
        System.out.println(
                "Email sent at: " +
                        LocalDateTime
                                .ofInstant(Instant
                                                .ofEpochMilli(
                                                        scheduledExecutionTime()),
                                        ZoneId.systemDefault()
                                ));
        var random = new Random();
        int value = random.ints(1, 7)
                .findFirst()
                .getAsInt();
        System.out.println("The duration of sending the mail will took: " +
                value);

        try {
            TimeUnit.SECONDS.sleep(value);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

