package generalVariable.auctionTask;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class AuctionAtomic {
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
                long time = new Random().nextLong(300);
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Notifier notifier = new Notifier();

    private final AtomicReference<Bid> latestBid
            = new AtomicReference<>(new Bid(1L, 1L, 1L));

    public AuctionAtomic(Long startedPrice) {
        latestBid.set(new Bid(0L, 0L, startedPrice));
    }

    public  boolean propose(Bid bid) {
        var sended=false;
        Bid current = this.latestBid.get();
        var result = false;

        while (bid.price > current.price && !result) {
            if(!sended) {
                notifier.sendOutdatedMessage(current);
                sended = true;
            }
            result = this.latestBid.compareAndSet(
                    current,bid);
            if(!result) {
                current = this.latestBid.get();
            }
        }
        return result;
    }

    public Bid getLatestBid() {
        return latestBid.get();
    }
}
