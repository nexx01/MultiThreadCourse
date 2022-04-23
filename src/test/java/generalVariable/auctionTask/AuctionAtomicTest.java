package generalVariable.auctionTask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static generalVariable.auctionTask.AuctionAtomic.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuctionAtomicTest {

    @Spy
    @InjectMocks
    AuctionAtomic auction =new AuctionAtomic(0L);

    @Mock
    Notifier notifier;

    @RepeatedTest(1000)
    void testAuctionAtomicDelay10() throws InterruptedException {
        int maxDelay = 2000;

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



}