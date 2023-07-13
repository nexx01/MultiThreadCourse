package customIterator;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomIteratorTest1 {
    @Test
    void givenListOfStrings_whenIteratedWithDefaultIterator() {
        var listOfString = List.of("hello", "world", "this", "is", "a", "test");
        var iterator = listOfString.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), "hello");
    }



    private static class Movie {
        private String name;
        private String director;
        private String rating;

        public String getName() {
            return name;
        }

        public String getDirector() {
            return director;
        }

        public String getRating() {
            return rating;
        }
    }
    private static class CustomIterator1{

    }



}




