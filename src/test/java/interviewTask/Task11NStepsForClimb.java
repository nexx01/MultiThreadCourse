package interviewTask;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Task11NStepsForClimb{

    @Test
    void name() {
        int actual1 = climbingStairs(0, 5);
        assertEquals(8, actual1);

        int actual2 = climbingStairs2(5);
        assertEquals(8, actual2);
    }

    private int climbingStairs2(int n) {
        if (n == 0) {
            return 0;
        }
        int[] stairs = new int[n + 1];
        stairs[1] = 1;
        stairs[2] = 2;
        for (int i = 3; i <= n; i++) {
            System.out.println(Arrays.toString(stairs));
            stairs[i] = stairs[i - 1] + stairs[i - 2];
        }
        System.out.println(Arrays.toString(stairs));
        return stairs[n];
    }

    int climbingStairs(int i, int n) {
        if (i > n) {
            return 0;
        }
        if(i==n) return 1;

        System.out.println("i = " + i);
        return climbingStairs(i + 1, n) + climbingStairs(i + 2, n);
    }


}
