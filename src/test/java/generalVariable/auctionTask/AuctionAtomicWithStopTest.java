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
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10);

        for (long i = 10; i > 0; i--) {
            long finalI = i;
            executorService.submit(() -> {
                try {
                    cyclicBarrier.await();
                    auction.propose(new AuctionAtomicWithThreadPoolUltimateStop.Bid(finalI, finalI, finalI));
                } catch (ExecutionException | InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executorService.awaitTermination(10, TimeUnit.MILLISECONDS);

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

        CyclicBarrier cyclicBarrier = new CyclicBarrier(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (long i = 0; i < 10; i++) {
            long finalI = i;
            executorService.submit(() -> {
                try {
                    cyclicBarrier.await();
                    auction.propose(new AuctionAtomicWithThreadPoolUltimateStop.Bid(finalI, finalI, finalI));
                } catch (ExecutionException | InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executorService.awaitTermination(60, TimeUnit.MILLISECONDS);

        assertAll(() -> {
            assertEquals(9L, auction.getLatestBid().price);
            assertEquals(9L, auction.getLatestBid().participantId);
            assertEquals(9L, auction.getLatestBid().id);
        });
    }

    @Test
    @DisplayName("Обновление цены в несколько потоков с остановкой")
    @RepeatedTest(1000)
    void testProposeConcurrentlyWithStop() throws InterruptedException, BrokenBarrierException {
        var maxDelay = 100;
        lenient().doAnswer(invocation -> {
                    Thread.sleep(new Random().nextInt(maxDelay));
                    return null;
                }
        ).when(notifier).sendOutdatedMessage(any(AuctionAtomicWithThreadPoolUltimateStop.Bid.class));

        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> auction.stopAuction());
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (long i = 0; i < 10; i++) {
            long finalI = i;
            executorService.submit(() -> {
                try {
                    auction.propose(new AuctionAtomicWithThreadPoolUltimateStop.Bid(finalI, finalI, finalI));
                    cyclicBarrier.await();
                } catch (ExecutionException | InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executorService.awaitTermination(100, TimeUnit.MILLISECONDS);

        assertAll(() -> {
            assertEquals(4L, auction.getLatestBid().price);
            assertEquals(4L, auction.getLatestBid().participantId);
            assertEquals(4L, auction.getLatestBid().id);
        });
    }
}