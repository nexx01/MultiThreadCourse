package generalVariable.auctionTask.additionExample;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuctionSingleThreadTest {
    @Test

    void name() {
        AuctionSingleThread auctionSingleThread = new AuctionSingleThread();

        auctionSingleThread.propose(new AuctionSingleThread.Bid(2l, 2l, 2l));
        auctionSingleThread.propose(new AuctionSingleThread.Bid(3l, 3l, 3l));
        auctionSingleThread.propose(new AuctionSingleThread.Bid(4l, 4l, 4l));
        auctionSingleThread.propose(new AuctionSingleThread.Bid(6l, 2l, 6l));
        auctionSingleThread.propose(new AuctionSingleThread.Bid(7l, 2l, 2l));
        auctionSingleThread.propose(new AuctionSingleThread.Bid(8l, 1l, 1l));

        assertAll(()->{
            assertEquals(6L, auctionSingleThread.getLatestBid().price);
            assertEquals(2L, auctionSingleThread.getLatestBid().participantId);
            assertEquals(6L, auctionSingleThread.getLatestBid().id);
        });
    }
}