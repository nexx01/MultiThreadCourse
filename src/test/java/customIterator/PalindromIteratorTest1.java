package customIterator;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PalindromIteratorTest1 {

    @Test
    void givenListOfString_whenPalindromIterator_thenOnlyParindromes() {
        var listOfStrings = List.of("oslo", "madam", "cat", "deed", "wow", "test");
        var palindromIterator = new PalindromIterator(listOfStrings);

        int count = 0;
        while (palindromIterator.hasNext()) {
            palindromIterator.next();
            count++;
        }
        assertEquals(count, 3);
    }



    private class PalindromIterator implements Iterator<String> {
        private int currentIndex;
        private List<String> list;

        public PalindromIterator(List<String> list) {
            this.list = list;
        }

        @Override
        public boolean hasNext() {
            while (currentIndex < list.size()) {
                String currString = list.get(currentIndex);
                if (isPalindrome(currString)) {
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

        private boolean isPalindrome(String input) {
            for (int i = 0; i < input.length()/2; i++) {
                if (input.charAt(i) != input.charAt(input.length() - i - 1)) {
                    return false;
                }
            }
            return true;
        }
    }
}
