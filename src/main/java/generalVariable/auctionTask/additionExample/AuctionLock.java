package generalVariable.auctionTask.additionExample;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AuctionLock {
    Lock lock = new ReentrantLock();

    public static class Bid {
        // учебный пример без private модификаторов и get методов
        Long id; // ID заявки
        Long participantId; // ID участника
        Long price; // предложенная цена

        public Bid(Long id, Long participantId, Long price) {
            this.id = id;
            this.participantId = participantId;
            this.price = price;
        }
    }

    public static class Notifier {
        public void sendOutdatedMessage(Bid bid) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Notifier notifier = new Notifier();

    private Bid latestBid=new Bid(1L, 1L, 1L);

    public  boolean propose(Bid bid) {
        try {
            lock.lock();
            if (bid.price > latestBid.price) {
                notifier.sendOutdatedMessage(latestBid);
                latestBid = bid;
                return true;
            }
            return false;
        }finally {
            lock.unlock();
        }
    }

    public Bid getLatestBid() {
        return latestBid;
    }
}
