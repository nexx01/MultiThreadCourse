package async;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static async.CompletableFuturePractice.PriceAggregator.DEFAULT_MIN_VALUE;
import static async.CompletableFuturePractice.PriceAggregator.MAX_TIME_WAITING_MILLIS;
import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
class CompletableFuturePracticeTest {
    @Spy
    @InjectMocks
    CompletableFuturePractice.PriceAggregator subj;

    @Mock
    CompletableFuturePractice.PriceRetriever priceRetriever;

    @Test
    @DisplayName("Если все запросы превысили таймаут")
    @Timeout(value = MAX_TIME_WAITING_MILLIS+100, unit = MILLISECONDS)
     void testAllPriceLongResponse() {
        doAnswer(invocation -> {
            Thread.sleep(MAX_TIME_WAITING_MILLIS + 1000);
            return 0.0;
        }).when(priceRetriever).getPrice(any(),any());

        double actual = subj.getMinPrice(1L);

        assertEquals(POSITIVE_INFINITY, actual);
    }

    @Test
    @DisplayName("Если один запрос вернул в пределах таймаута")
    @Timeout(value = MAX_TIME_WAITING_MILLIS+100, unit = MILLISECONDS)
    void testOnlyOneResponseWintoutTimeout() {
        var expected = 100.0;
        doAnswer(invocation -> {
            Thread.sleep(MAX_TIME_WAITING_MILLIS + 1000);
            return Double.MIN_VALUE;
        }).when(priceRetriever).getPrice(any(),any());

        doAnswer(invocation -> {
            return expected;
        }).when(priceRetriever).getPrice(1L,10l);

        double actual = subj.getMinPrice(1L);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Если все запросы вернулись в пределах таймаута")
    @Timeout(value = MAX_TIME_WAITING_MILLIS+100, unit = MILLISECONDS)
    void testAllResponseWithoutTimeout() {
        var expected = 100.0;
        doAnswer(invocation -> ThreadLocalRandom.current().nextDouble(expected,MAX_VALUE))
                .when(priceRetriever).getPrice(any(),any());

        doAnswer(invocation -> expected).when(priceRetriever).getPrice(1L,10l);

        double actual = subj.getMinPrice(1L);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Если все запросы вернулись в пределах таймаута")
    @Timeout(value = MAX_TIME_WAITING_MILLIS+100, unit = MILLISECONDS)
    void testVeryMoreTask() {
        Set<Long> collect = ThreadLocalRandom.current().longs(1000)
                .boxed()
                .collect(Collectors.toSet());
        collect.add(10l);
        subj.shopIds = collect;
        var expected = 100.0;

        doAnswer(invocation -> ThreadLocalRandom.current().nextDouble(expected,MAX_VALUE))
                .when(priceRetriever).getPrice(any(),any());

        doAnswer(invocation -> expected).when(priceRetriever).getPrice(1L,10l);

        double actual = subj.getMinPrice(1L);

        assertEquals(expected, actual);
    }


}