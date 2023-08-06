package customIterator;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PalindromeIteratorTest2 {

    @Test
    void name() {
        var listOfString = List.of("oslo", "tet", "asddsa");
        int count = 0;
        var palindromeIterator = new PalindromeIterator(listOfString);
        while (palindromeIterator.hasNext()) {
            palindromeIterator.next();
            count++;
        }

        assertEquals(count, 2);
    }

    private static class PalindromeIterator implements Iterator<String> {


        private int currentIndex;
        private List<String> list;

        public PalindromeIterator(List<String> list) {
            this.list = list;
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
            while (currentIndex < list.size()) {
                String currentStr = list.get(currentIndex);
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
            return list.get(currentIndex++);
        }


    }
}

