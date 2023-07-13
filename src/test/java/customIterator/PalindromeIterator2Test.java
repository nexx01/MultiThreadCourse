package customIterator;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PalindromeIterator2Test {

    @Test
    void givenListOfString_whinPalindromIterator_thenOnlyPalindromes() {

        var listOfString = List.of("oslo", "test", "assa", "qwe", "qweewq");
        var palindromeIterator2 = new PalindromeIterator2(listOfString);
        int count = 0;
        while (palindromeIterator2.hasNext()) {
            palindromeIterator2.next();
            count++;
        }
        assertEquals(count, 2);
    }

    private class PalindromeIterator2 implements Iterator<String> {


        private int currentIndex;
        private List<String> listOfString;

        public PalindromeIterator2(List<String> listOfString) {
            this.listOfString = listOfString;
        }

        private boolean isPalindrome(String input) {
            for (int i = 0; i < input.length() / 2; i++) {
                if (input.charAt(i) != input.charAt(input.length() - i - 1)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean hasNext() {
            while (currentIndex < listOfString.size()) {
                var currentStr = listOfString.get(currentIndex);
                if (isPalindrome(currentStr)) {
                    return true;
                }
                currentIndex++;
            }
            return false;
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return listOfString.get(currentIndex++);
        }
    }
}

