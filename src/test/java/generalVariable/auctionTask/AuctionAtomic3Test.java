package generalVariable.auctionTask;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static generalVariable.auctionTask.AuctionAtomic3.Bid;
import static generalVariable.auctionTask.AuctionAtomic3.Notifier;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
class AuctionAtomic3Test {

    @Spy
    @InjectMocks
    AuctionAtomic3 auction =new AuctionAtomic3(0L);

    @Mock
    Notifier notifier;

    @RepeatedTest(1000)
    void testAuctionAtomic2Delay10() throws InterruptedException {
        int maxDelay = 2;

        doAnswer(invocation->{
            Thread.sleep(new Random().nextInt(maxDelay));
            return null;
                }
        ).when(notifier).sendOutdatedMessage(any(Bid.class));

        long start = System.currentTimeMillis();
        var fixedThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        fixedThreadPool.submit(() -> auction.propose(new Bid(2l, 2l, 2l)));
        fixedThreadPool.submit(() -> auction.propose(new Bid(3l, 3l, 3l)));
        fixedThreadPool.submit(() -> auction.propose(new Bid(4l, 4l, 4l)));
        fixedThreadPool.submit(() -> auction.propose(new Bid(6l, 2l, 6l)));
        fixedThreadPool.submit(() -> auction.propose(new Bid(7l, 2l, 2l)));
        fixedThreadPool.submit(() -> auction.propose(new Bid(11l, 11l, 10l)));
        fixedThreadPool.submit(() -> auction.propose(new Bid(8l, 1l, 1l)));
        fixedThreadPool.submit(() -> auction.propose(new Bid(9l, 1l, 1l)));
        fixedThreadPool.submit(() -> auction.propose(new Bid(10l, 1l, 1l)));
        fixedThreadPool.submit(() -> auction.propose(new Bid(12l, 21l, 21l)));

        while (fixedThreadPool.getActiveCount() > 0) {
            Thread.sleep(100);
        }

        System.out.println("-------------------------------------------");
        System.out.println(System.currentTimeMillis() - start);
        System.out.println("-------------------------------------------");

        assertAll(()->{
            assertEquals(21L, auction.getLatestBid().price);
            assertEquals(21L, auction.getLatestBid().participantId);
            assertEquals(12L, auction.getLatestBid().id);
        });
    }

    @Test
    void name() throws InterruptedException {
        class Test{
//            ThreadLocal<Random> randomThreadLocal =
//                    ThreadLocal.withInitial(Random::new);
        }
//         class ThreadLocalAwareThreadPool extends ThreadPoolExecutor {
//
//             public ThreadLocalAwareThreadPool(int corePoolSize,
//                                               int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
//                 super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
//             }
//
//             @Override
//            protected void afterExecute(Runnable r, Throwable t) {
//
//            }
//        }

        ThreadPoolExecutor fixedThreadPool =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

//        ThreadLocalAwareThreadPool fixedThreadPool =
//                new ThreadLocalAwareThreadPool(10, 10, 60,
//                        TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            fixedThreadPool.submit(() ->{
                Test test = new Test();

            });
        }

        TimeUnit.SECONDS.sleep(20);
//        }
    }
}