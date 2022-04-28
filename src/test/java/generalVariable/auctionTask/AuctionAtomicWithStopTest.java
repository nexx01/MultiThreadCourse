package generalVariable.auctionTask;

import generalVariable.auctionTask.AuctionAtomicWithStop.Bid;
import generalVariable.auctionTask.AuctionAtomicWithStop.Notifier;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class AuctionAtomicWithStopTest {

    @Spy
    @InjectMocks
    AuctionAtomicWithStop auction = new AuctionAtomicWithStop(0L);

    @Mock
    Notifier notifier;

    @RepeatedTest(100)
    void testAuctionAtomicWithStop() throws InterruptedException {
        int maxDelay = 1;

        lenient().doAnswer(invocation->{
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
        Thread.sleep(100);
        auction.stopAuction();
        fixedThreadPool.submit(() -> auction.propose(new Bid(13l, 21l, 21l)));
        fixedThreadPool.submit(() -> auction.propose(new Bid(14l, 1l, 88l)));
        fixedThreadPool.submit(() -> auction.propose(new Bid(15l, 1l, 99l)));

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
}