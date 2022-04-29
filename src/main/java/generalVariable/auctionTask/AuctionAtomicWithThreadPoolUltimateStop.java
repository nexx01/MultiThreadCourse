package generalVariable.auctionTask;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;


public class AuctionAtomicWithThreadPoolUltimateStop {
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

    //Признак, остановки аукциона
    private volatile boolean isStopped;
    //Последняя установленная цена
    private AtomicMarkableReference<Bid> latestBid;
    //Пул для установки цены
    static  ExecutorService fixedThreadPool = Executors.newFixedThreadPool(16) ;
    // Пул для отправки сообщения
    static final ThreadPoolExecutor cachedThreadPool =
            (ThreadPoolExecutor) Executors.newCachedThreadPool();

    static {
        cachedThreadPool.setCorePoolSize(100);
    }


    public AuctionAtomicWithThreadPoolUltimateStop(Long startedPrice, Integer concurrentLevel) {
        this.latestBid= new AtomicMarkableReference<>(new Bid(0L, 0L, startedPrice),false);
        fixedThreadPool= Executors.newFixedThreadPool(concurrentLevel);
    }


    public boolean propose(Bid bid) throws ExecutionException,
            InterruptedException {

        if (latestBid.isMarked()&& bid.price > latestBid.getReference().price) {

            //пытаемся обновить
            return fixedThreadPool.submit(() -> {
                var res = false;
                Bid current = this.latestBid.getReference();
                while ( bid.price > current.price && !res) {
                    res = this.latestBid.compareAndSet(current, bid,false,false);
                    if (res) {
                        // если удалось обновить, отправляем сообщение
                        Bid finalCurrent = current;
                        cachedThreadPool.submit(() -> notifier.sendOutdatedMessage(finalCurrent));
                    } else {
                        current = this.latestBid.getReference();
                    }
                }
                return res;
            }).get();

        }
        return false;
    }

    public Bid getLatestBid() {
        return latestBid.getReference();
    }

    public void stopAuction() {
        while (latestBid.attemptMark(latestBid.getReference(), true)) {}
    }
}
