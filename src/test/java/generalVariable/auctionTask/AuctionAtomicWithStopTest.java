package generalVariable.auctionTask;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class AuctionAtomicWithStopTest {

    @Spy
    @InjectMocks
    AuctionAtomicWithThreadPoolUltimateStop auction = new AuctionAtomicWithThreadPoolUltimateStop(0L, 10);

    @Mock
    AuctionAtomicWithThreadPoolUltimateStop.Notifier notifier;

    @Test
    void testProposePrice_InOneThread() throws ExecutionException, InterruptedException {
        var expected = 100l;

        auction.propose(new AuctionAtomicWithThreadPoolUltimateStop.Bid(expected, expected, expected));

        assertAll(() -> {
            assertEquals(expected, auction.getLatestBid().price);
            assertEquals(expected, auction.getLatestBid().participantId);
            assertEquals(expected, auction.getLatestBid().id);
        });
    }

    @Test
    void testProposePrice_InOneThreadWithStop() throws InterruptedException, ExecutionException {
        var expected = 100l;

        auction.propose(new AuctionAtomicWithThreadPoolUltimateStop.Bid(expected, expected, expected));
        auction.stopAuction();
        auction.propose(new AuctionAtomicWithThreadPoolUltimateStop.Bid(expected + 10, expected + 10, expected + 10));

        assertAll(() -> {
            assertEquals(expected, auction.getLatestBid().price);
            assertEquals(expected, auction.getLatestBid().participantId);
            assertEquals(expected, auction.getLatestBid().id);
        });
    }

    @Test
    void testLowConcurrentlyProposePrice() throws InterruptedException {
        var maxDelay = 1;
        lenient().doAnswer(invocation -> {
                    Thread.sleep(new Random().nextInt(maxDelay));
                    return null;
                }
        ).when(notifier).sendOutdatedMessage(any(AuctionAtomicWithThreadPoolUltimateStop.Bid.class));

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);

        for (long i = 10; i > 0; i--) {
            long finalI = i;
            executorService.submit (() -> {
                countDownLatch.countDown();
                try {
                    auction.propose(new AuctionAtomicWithThreadPoolUltimateStop.Bid(finalI, finalI, finalI));
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

            countDownLatch.await();

        assertAll(() -> {
            assertEquals(10L, auction.getLatestBid().price);
            assertEquals(10L, auction.getLatestBid().participantId);
            assertEquals(10L, auction.getLatestBid().id);
        });
    }

    @Test
    void testConcurrentlyProposePrice() throws InterruptedException {
        var maxDelay = 100;
        lenient().doAnswer(invocation -> {
                    Thread.sleep(new Random().nextInt(maxDelay));
                    return null;
                }
        ).when(notifier).sendOutdatedMessage(any(AuctionAtomicWithThreadPoolUltimateStop.Bid.class));

        CountDownLatch countDownLatch = new CountDownLatch(10);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10);

        for (long i = 10; i > 0; i--) {
            long finalI = i;
            new Thread(() -> {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
                try {
                    auction.propose(new AuctionAtomicWithThreadPoolUltimateStop.Bid(finalI, finalI, finalI));
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();

        assertAll(() -> {
            assertEquals(10L, auction.getLatestBid().price);
            assertEquals(10L, auction.getLatestBid().participantId);
            assertEquals(10L, auction.getLatestBid().id);
        });
    }

    @Test
    @DisplayName("Обновление цены в несколько потоков с остановкой")
    void testProposeConcurrentlyWithStop() throws InterruptedException, BrokenBarrierException {
        var maxDelay = 100;
        lenient().doAnswer(invocation -> {
                    Thread.sleep(new Random().nextInt(maxDelay));
                    return null;
                }
        ).when(notifier).sendOutdatedMessage(any(AuctionAtomicWithThreadPoolUltimateStop.Bid.class));

        CountDownLatch countDownLatch = new CountDownLatch(5);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5,()->{
            try {
                countDownLatch.await();
                auction.stopAuction();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (long i = 0; i < 10; i++) {
            long finalI = i;
            executorService.submit(() -> {
                try {
                    auction.propose(new AuctionAtomicWithThreadPoolUltimateStop.Bid(finalI, finalI, finalI));
                    countDownLatch.countDown();
                    cyclicBarrier.await();
                } catch (ExecutionException | InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        cyclicBarrier.await();

        assertAll(() -> {
            assertEquals(4L, auction.getLatestBid().price);
            assertEquals(4L, auction.getLatestBid().participantId);
            assertEquals(4L, auction.getLatestBid().id);
        });
    }
}