package generalVariable.auctionTask.additionExample;

import org.junit.jupiter.api.RepeatedTest;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.jupiter.api.Assertions.*;

class AuctionSynchronizedTest {
    @RepeatedTest(1000)
    void name() throws InterruptedException {
        long start = System.currentTimeMillis();
        AuctionSynchronized auction = new AuctionSynchronized(); //400-700


        var fixedThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        fixedThreadPool.submit(() -> auction.propose(new AuctionSynchronized.Bid(2l, 2l, 2l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionSynchronized.Bid(3l, 3l, 3l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionSynchronized.Bid(4l, 4l, 4l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionSynchronized.Bid(6l, 2l, 6l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionSynchronized.Bid(7l, 2l, 2l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionSynchronized.Bid(11l, 11l, 10l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionSynchronized.Bid(8l, 1l, 1l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionSynchronized.Bid(9l, 1l, 1l)));
        fixedThreadPool.submit(() -> auction.propose(new AuctionSynchronized.Bid(10l, 1l, 1l)));

        while (fixedThreadPool.getActiveCount() > 0) {
            Thread.sleep(100);
        }

        System.out.println("-------------------------------------------");
        System.out.println(System.currentTimeMillis() - start);
        System.out.println("-------------------------------------------");

        assertAll(()->{
            assertEquals(10L, auction.getLatestBid().price);
            assertEquals(11L, auction.getLatestBid().participantId);
            assertEquals(11L, auction.getLatestBid().id);
        });
    }
}