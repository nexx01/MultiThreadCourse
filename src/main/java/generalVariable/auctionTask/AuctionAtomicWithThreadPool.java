package generalVariable.auctionTask;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;


public class AuctionAtomicWithThreadPool {
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

    private AtomicReference<Bid> latestBid = new AtomicReference<>();
    //Пул для установки цены
    static  ExecutorService fixedThreadPool = Executors.newFixedThreadPool(16) ;
    // Пул для отправки сообщения
    static final ThreadPoolExecutor cachedThreadPool =
            (ThreadPoolExecutor) Executors.newCachedThreadPool();

    static {
        cachedThreadPool.setCorePoolSize(100);
    }


    public AuctionAtomicWithThreadPool(Long startedPrice, Integer concurrentLevel) {
        this.latestBid.set(new Bid(0L, 0L, startedPrice));
        fixedThreadPool= Executors.newFixedThreadPool(concurrentLevel);
    }


    public boolean propose(Bid bid) throws ExecutionException,
            InterruptedException {

        if (bid.price > latestBid.get().price) {
            //отправляем сообщение
            cachedThreadPool.submit(() -> notifier.sendOutdatedMessage(latestBid.get()));
            //пытаемся обновить
            return fixedThreadPool.submit(() -> {
                var res = false;
                Bid current = this.latestBid.get();
                while (bid.price > current.price && !res) {
                    res = this.latestBid.compareAndSet(
                            current, bid);
                    if (!res) {
                        current = this.latestBid.get();
                    }
                }
                return res;
            }).get();

        }
        return false;
    }

    public Bid getLatestBid() {
        return latestBid.get();
    }
}
