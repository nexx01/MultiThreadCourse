package generalVariable.auctionTask;

import org.junit.jupiter.api.RepeatedTest;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.jupiter.api.Assertions.*;

class AuctionAtomicTest {

    @RepeatedTest(1000)
    void testAuctionAtomic() throws InterruptedException {
        long start = System.currentTimeMillis();
        AuctionAtomic auction = new AuctionAtomic(0L); //850-1100
        var fixedThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomic.Bid(2l, 2l, 2l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomic.Bid(3l, 3l, 3l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomic.Bid(4l, 4l, 4l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomic.Bid(6l, 2l, 6l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomic.Bid(7l, 2l, 2l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomic.Bid(11l, 11l, 10l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomic.Bid(8l, 1l, 1l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomic.Bid(9l, 1l, 1l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomic.Bid(10l, 1l, 1l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomic.Bid(12l, 21l, 21l)));

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