package interviewTask;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetProductOfAllOtherElements {

    @Test
    void name() {
        assertTrue(Arrays.equals(new int[]{120,60,40,30,24}, getProducts(new int[]{1,2,3,4,5})));
        assertTrue(Arrays.equals(new int[]{120,60,40,30,24}, productExceptSelf(new int[]{1,2,3,4,5})));
    }

    int[] getProducts(int[] ar) {
        int m = 1;
        for (int i = 0; i < ar.length; i++) {
            m *= ar[i];
        }

        int[] res=new int[ar.length];
        for (int i = 0; i < ar.length; i++) {
            res[i] = m / ar[i];
        }
        return res;
    }

    public int[] productExceptSelf(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }
        int len = nums.length;
        //Store prefix product
        int[] pre = new int[len];
        //The first element has no prefix, so assign its prefix product to 1
        pre[0] = 1;
        //Store suffix product
        int[] suf = new int[len];
        //The last element has no suffix, so assign 1 to its suffix product
        suf[len - 1] = 1;
        //The first traversal, calculate the prefix product of all elements
        for (int i = 0; i < len - 1; i++) {
            pre[i + 1] = pre[i] * nums[i];
        }
        //The second traversal is to calculate the suffix product of all elements. In fact, the two traversals can be combined into one traversal
        for (int i = len-1; i > 0; i--) {
            suf[i - 1] = suf[i] * nums[i];
        }
        //Array to store the final result
        int[] rs = new int[len];
        //Multiply the prefix product and the suffix product of each element is the answer
        for (int i = 0; i < len; i++) {
            rs[i] = pre[i] * suf[i];
        }
        return rs;
    }
    static void reverse(int[] array) {
        Collections.reverse(Arrays.asList(array));


    }
}
