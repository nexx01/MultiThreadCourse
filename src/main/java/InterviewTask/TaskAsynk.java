package InterviewTask;

import java.util.concurrent.CompletableFuture;

public class TaskAsynk {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        var i1 = longTask();
        var i2 = longTask();
        long end = System.currentTimeMillis();
        long firstStep = end - start;

        long start2 = System.currentTimeMillis();
        var first = CompletableFuture.runAsync(TaskAsynk::longTask);
        var second = CompletableFuture.runAsync(TaskAsynk::longTask);

        CompletableFuture.allOf(first, second).join();

        long end2 = System.currentTimeMillis();
        long secondStep = end2 - start2;

        long start3 = System.currentTimeMillis();
        CompletableFuture.runAsync(TaskAsynk::longTask)
                .thenCompose(d -> CompletableFuture.runAsync(TaskAsynk::longTask))
                .join();

        long end3 = System.currentTimeMillis();
        long thirdStep = end3 - start3;

        System.out.println("firstStep take " + firstStep + " ms");
        System.out.println("secondStep take " + secondStep + " ms");
        System.out.println("thirdStep take " + thirdStep + " ms");

    }

    private static int longTask() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

}
