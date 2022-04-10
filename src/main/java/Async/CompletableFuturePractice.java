package Async;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.lang.Double.POSITIVE_INFINITY;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class CompletableFuturePractice {

    public static class PriceRetriever {

        public double getPrice(long itemId, long shopId) {
            // имитация долгого HTTP-запроса
            int delay = ThreadLocalRandom.current().nextInt(10);
            try {
                Thread.sleep(delay * 1000);
            } catch (InterruptedException e) {
            }

            return ThreadLocalRandom.current().nextDouble(1000);
        }
    }

    public static class PriceAggregator {

        /**
         * Максимальное время ожидание ответа
         */
        public static final int MAX_TIME_WAITING_MILLIS = 3000;
        /**
         * Минимальная цена по умолчанию
         */
        public static final double DEFAULT_MIN_VALUE = 0.00;

        private PriceRetriever priceRetriever = new PriceRetriever();

        private Set<Long> shopIds = Set.of(10l, 45l, 66l, 345l, 234l, 333l,
                67l, 123l, 768l);

        public double getMinPrice(long itemId) {
            // здесь будет ваш код

            List<CompletableFuture<Double>> collect = shopIds.stream()
                    .map(id -> CompletableFuture.supplyAsync(
                                    () -> priceRetriever.getPrice(itemId, id))
                            .completeOnTimeout(POSITIVE_INFINITY, MAX_TIME_WAITING_MILLIS,MILLISECONDS)
                    )
                    .collect(Collectors.toList());

            return CompletableFuture.allOf(collect.toArray(new CompletableFuture[0]))
                    .thenApply(v -> collect.stream()
                            .mapToDouble(CompletableFuture::join)
                            .min()
                            .orElse(DEFAULT_MIN_VALUE))
                    .join();
        }
    }

    public static void main(String[] args) {
        PriceAggregator priceAggregator = new PriceAggregator();
        long itemId = 12l;


        long start = System.currentTimeMillis();
        double min = priceAggregator.getMinPrice(itemId);
        long end = System.currentTimeMillis();

        System.out.println(min);
        System.out.println((end - start) < 3000); // should be true
    }
}

