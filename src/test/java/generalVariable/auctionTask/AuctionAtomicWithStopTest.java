package generalVariable.auctionTask;

import org.junit.jupiter.api.RepeatedTest;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.jupiter.api.Assertions.*;

class AuctionAtomicWithStopTest {

    @RepeatedTest(1000)
    void testAuctionAtomicWithStop() throws InterruptedException {
        long start = System.currentTimeMillis();
        AuctionAtomicWithStop auction = new AuctionAtomicWithStop(0L); //850-1100

        var fixedThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomicWithStop.Bid(2l, 2l, 2l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomicWithStop.Bid(3l, 3l, 3l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomicWithStop.Bid(4l, 4l, 4l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomicWithStop.Bid(6l, 2l, 6l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomicWithStop.Bid(7l, 2l, 2l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomicWithStop.Bid(11l, 11l, 10l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomicWithStop.Bid(8l, 1l, 1l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomicWithStop.Bid(9l, 1l, 1l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomicWithStop.Bid(10l, 1l, 1l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomicWithStop.Bid(12l, 21l, 21l)));
        while (fixedThreadPool.getActiveCount() > 0) {}
        auction.stopAuction();
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomicWithStop.Bid(13l, 21l, 21l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomicWithStop.Bid(14l, 1l, 88l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionAtomicWithStop.Bid(15l, 1l, 99l)));


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