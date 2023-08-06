package customIterator;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomIteratorTest2 {
    @Test
    void ds() {
        var movies = List.of(
                new Movie("1", "1d", 1),
                new Movie("2", "2d", 20),
                new Movie("3", "3d", 8)
        );

        var customIterator2 = new CustomIterator2(movies);

        int count = 0;
        while (customIterator2.hasNext()) {
            count++;
            customIterator2.next();
        }

        assertEquals(2,count);

    }


}

class CustomIterator2 implements Iterator<Movie>{

    private int currentIndex;
    private List<Movie> list;

    public CustomIterator2(List<Movie> list) {
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        while (currentIndex < list.size()) {
            var currentMovie = list.get(currentIndex);

            if (isHighRating(currentMovie)) {
                return true;
            }
            currentIndex++;
        }
        return false;
    }

    @Override
    public Movie next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return list.get(currentIndex++);
    }

    private boolean isHighRating(Movie movie) {
        return movie.rating() >= 8;
    }
}

record Movie(String name, String director, float rating) {

}
