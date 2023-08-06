package interviewTask;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LargestSumTest {


    @Test
    void name() {
        assertEquals(13, getLargestSum(new int[]{2, 4, 6, 2, 5}));
        assertEquals(10, getLargestSum(new int[]{5, 1, 1, 5}));
//        assertEquals(13, new int[]{});
//        assertEquals(13, new int[]{});
    }


    int getLargestSum(int[] ar) {
        if (ar == null || ar.length == 0) {
            return 0;
        }

        if (ar.length == 1) {
            return ar[0];
        }

        int len = ar.length;
        int[] dp = new int[len];

        dp[0] = Math.max(0, ar[0]);
        dp[1] = Math.max(0, Math.max(ar[0], ar[1]));

        for (int i = 2; i < len; i++) {
            if (ar[i] > 0) {
                dp[i] = Math.max(dp[i - 2] + ar[i], dp[i - 1]);
            } else {
                dp[i] = dp[i - 1];
            }
        }


        return dp[len - 1];
    }
}
