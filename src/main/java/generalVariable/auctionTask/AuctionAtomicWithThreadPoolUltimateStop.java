package generalVariable.auctionTask;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
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
    private AtomicReference<Bid> latestBid = new AtomicReference<>();
    //Пул для установки цены
    static  ExecutorService fixedThreadPool = Executors.newFixedThreadPool(16) ;
    // Пул для отправки сообщения
    static final ThreadPoolExecutor cachedThreadPool =
            (ThreadPoolExecutor) Executors.newCachedThreadPool();

    static {
        cachedThreadPool.setCorePoolSize(100);
    }


    public AuctionAtomicWithThreadPoolUltimateStop(Long startedPrice, Integer concurrentLevel) {
        this.latestBid.set(new Bid(0L, 0L, startedPrice));
        fixedThreadPool= Executors.newFixedThreadPool(concurrentLevel);
    }


    public boolean propose(Bid bid) throws ExecutionException,
            InterruptedException {

        if (!isStopped && bid.price > latestBid.get().price) {

            //пытаемся обновить
            return fixedThreadPool.submit(() -> {
                var res = false;
                Bid current = this.latestBid.get();
                while (!isStopped && bid.price > current.price && !res) {
                    res = this.latestBid.compareAndSet(
                            current, bid);
                    if (res) {
                        // если удалось обновить, отправляем сообщение
                        Bid finalCurrent = current;
                        cachedThreadPool.submit(() -> notifier.sendOutdatedMessage(finalCurrent));
                    } else {
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

    public void stopAuction() {
        fixedThreadPool.shutdown();
        isStopped = true;
    }
}
