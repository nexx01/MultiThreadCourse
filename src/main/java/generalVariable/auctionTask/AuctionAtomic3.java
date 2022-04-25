package generalVariable.auctionTask;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAdder;

public class AuctionAtomic3 {
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
        }
    }

    private Notifier notifier = new Notifier();

    private final AtomicReference<Bid> latestBid = new AtomicReference<>();
    private  LongAdder longAdder = new LongAdder();

    public AuctionAtomic3(Long startedPrice) {
        latestBid.set(new Bid(0L, 0L, startedPrice));
    }

    ExecutorService executorService = Executors.newCachedThreadPool();

    public boolean propose(Bid bid) {
        Bid current = this.latestBid.get();
        var result = false;

        while (bid.price > current.price && !result) {
            Bid finalCurrent = current;
            executorService.submit(() -> notifier.sendOutdatedMessage(finalCurrent));
            result = this.latestBid.compareAndSet(
                    current, bid);
            if (!result) {
                current = this.latestBid.get();
            }
        }

        return result;
    }

    public Bid getLatestBid() {
        return latestBid.get();
    }
}
