package customIterator;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomIteratorTest {

    @Test
    void givenMovieList_whenMovieIteratorUsed_thenOnlyHighRatedMovies() {
        var movies = List.of(
                new Movie("1", "1d", 1),
                new Movie("2", "2d", 2),
                new Movie("4", "3d", 8)
        );

        var customIterator = new CustomIterator(movies);
        int counnt = 0;
        while (customIterator.hasNext()) {
            counnt++;
            customIterator.next();
        }

        assertEquals(counnt, 1);
    }

    public static class CustomIterator implements Iterator<Movie> {


        private int currentIndex;
        private List<Movie> list;

        public CustomIterator(List<Movie> list) {
            this.list = list;
        }

        @Override
        public boolean hasNext() {
            while (currentIndex < list.size()) {
                var currentMovie = list.get(currentIndex);
                if (isMovieEligible(currentMovie)) {
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

        private boolean isMovieEligible(Movie movie) {
            return movie.getRating() >= 8;
        }
    }


    public static class Movie{
        private String name;
        private String director;
        private float rating;

        public Movie(String name, String director, float rating) {
            this.name = name;
            this.director = director;
            this.rating = rating;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
        }

        public float getRating() {
            return rating;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }
    }

}

