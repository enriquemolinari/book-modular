package movies.model;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import movies.api.MovieInfo;
import movies.api.MoviesException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.YearMonth;

import static movies.main.PersistenceUnit.DERBY_EMBEDDED_SHOWS_MODULE;
import static movies.model.ForTests.*;
import static org.junit.jupiter.api.Assertions.*;

public class MoviesTest {

    private static final String JOSEUSER_SURNAME = "aSurname";
    private static final String JOSEUSER_NAME = "Jose";
    private static final String JOSEUSER_PASS = "password12345679";
    private static final String JOSEUSER_EMAIL = "jose@bla.com";
    private static final YearMonth JOSEUSER_CREDIT_CARD_EXPIRITY = YearMonth.of(
            LocalDateTime.now().getYear(),
            LocalDateTime.now().plusMonths(2).getMonth());
    private static final String JOSEUSER_CREDIT_CARD_SEC_CODE = "145";
    private static final String JOSEUSER_CREDIT_CARD_NUMBER = "123-456-789";
    private static final String JOSEUSER_USERNAME = "joseuser";
    private static final Long NON_EXISTENT_ID = -2L;
    private static final String ANTONIOUSER_USERNAME = "antonio";
    private static EntityManagerFactory emf;
    private final ForTests tests = new ForTests();

    @BeforeEach
    public void setUp() {
        emf = Persistence.createEntityManagerFactory(DERBY_EMBEDDED_SHOWS_MODULE);
    }

    @Test
    public void rateMovie() {
        var movies = new Movies(emf);
        var movieInfo = tests.createSuperMovie(movies);
        var joseId = registerUserJose(movies);
        var userRate = movies.rateMovieBy(joseId, movieInfo.id(), 4,
                "great movie");
        assertEquals(JOSEUSER_USERNAME, userRate.username());
        assertEquals(4, userRate.rateValue());
    }

    @Test
    public void retrieveRatesInvalidPageNumber() {
        var movies = new Movies(emf, 10 /* page size */);
        var e = assertThrows(MoviesException.class, () -> {
            movies.pagedRatesOfOrderedDate(1L, 0);
        });
        assertEquals(Movies.PAGE_NUMBER_MUST_BE_GREATER_THAN_ZERO,
                e.getMessage());
    }

    @Test
    public void retrievePagedRatesFromMovie() {
        var movies = new Movies(emf, 2 /* page size */);
        var movieInfo = tests.createSuperMovie(movies);
        var joseId = registerUserJose(movies);
        var userId = registerAUser(movies);
        var antonioId = registerUserAntonio(movies);
        movies.rateMovieBy(userId, movieInfo.id(), 1, "very bad movie");
        movies.rateMovieBy(joseId, movieInfo.id(), 2, "bad movie");
        movies.rateMovieBy(antonioId, movieInfo.id(), 3, "regular movie");
        var userRates = movies.pagedRatesOfOrderedDate(movieInfo.id(), 1);
        assertEquals(2, userRates.size());
        assertEquals(ANTONIOUSER_USERNAME, userRates.get(0).username());
        assertEquals(JOSEUSER_USERNAME, userRates.get(1).username());
    }

    @Test
    public void retrieveAllPagedRates() {
        var movies = new Movies(emf, 2 /* page size */);
        var superMovieInfo = tests.createSuperMovie(movies);
        var otherMovieInfo = tests.createOtherSuperMovie(movies);
        var joseId = registerUserJose(movies);
        movies.rateMovieBy(joseId, superMovieInfo.id(), 1, "very bad movie");
        movies.rateMovieBy(joseId, otherMovieInfo.id(), 3, "fine movie");
        var moviesList = movies.pagedMoviesSortedByRate(1);
        assertEquals(2, moviesList.size());
        assertEquals(ForTests.OTHER_SUPER_MOVIE_NAME, moviesList.get(0).name());
        assertEquals(ForTests.SUPER_MOVIE_NAME, moviesList.get(1).name());
    }

    @Test
    public void rateTheSameMovieTwice() {
        var movies = new Movies(emf);
        var movieInfo = tests.createSuperMovie(movies);
        var joseId = registerUserJose(movies);
        movies.rateMovieBy(joseId, movieInfo.id(), 4, "great movie");
        var e = assertThrows(MoviesException.class, () -> {
            movies.rateMovieBy(joseId, movieInfo.id(), 4, "great movie");
            fail("I was able to rate the same movie twice");
        });
        assertEquals(Movies.USER_HAS_ALREADY_RATE, e.getMessage());
    }

    @Test
    public void retrieveMovie() {
        var movies = new Movies(emf);
        var superMovie = tests.createSuperMovie(movies);
        MovieInfo movie = movies.movie(superMovie.id());
        assertEquals(2, movie.actors().size());
        assertEquals(1, movie.directorNames().size());
        assertEquals(SUPER_MOVIE_DIRECTOR_NAME, movie.directorNames().get(0));
        assertTrue(movie.actors()
                .contains(SUPER_MOVIE_ACTOR_CARLOS));
        assertEquals(SUPER_MOVIE_NAME, movie.name());
        assertEquals(SUPER_MOVIE_PLOT, movie.plot());
    }

    @Test
    public void moviesSortedByReleaseDate() {
        var movies = new Movies(emf, 1 /* page size */);
        tests.createSuperMovie(movies);
        tests.createOtherSuperMovie(movies);
        var moviesList = movies.pagedMoviesSortedByReleaseDate(1);
        assertEquals(1, moviesList.size());
        assertEquals(SUPER_MOVIE_NAME, moviesList.get(0).name());
    }

    @Test
    public void retrieveAllMovies() {
        var movies = new Movies(emf, 1 /* page size */);
        tests.createSuperMovie(movies);
        tests.createOtherSuperMovie(movies);
        var moviesList = movies.pagedMoviesSortedByName(1);
        assertEquals(1, moviesList.size());
        assertEquals(SUPER_MOVIE_NAME, moviesList.get(0).name());
        assertEquals(2, moviesList.get(0).genres().size());
        assertEquals(2, moviesList.get(0).actors().size());
        var moviesList2 = movies.pagedMoviesSortedByName(2);
        assertEquals(1, moviesList2.size());
        assertEquals(OTHER_SUPER_MOVIE_NAME, moviesList2.get(0).name());
        assertEquals(2, moviesList2.get(0).genres().size());
        assertEquals(1, moviesList2.get(0).actors().size());
    }

    @Test
    public void searchMovieByName() {
        var movies = new Movies(emf, 10 /* page size */);
        tests.createSuperMovie(movies);
        tests.createOtherSuperMovie(movies);
        var moviesList = movies.pagedSearchMovieByName("another", 1);
        assertEquals(1, moviesList.size());
        assertEquals(OTHER_SUPER_MOVIE_NAME, moviesList.get(0).name());
    }

    @Test
    public void searchMovieByNameNotFound() {
        var movies = new Movies(emf, 10 /* page size */);
        tests.createSuperMovie(movies);
        tests.createOtherSuperMovie(movies);
        var moviesList = movies.pagedSearchMovieByName("not_found_movie", 1);
        assertEquals(0, moviesList.size());
    }

    @Test
    public void movieIdNotExists() {
        var movies = new Movies(emf, 10 /* page size */);
        var e = assertThrows(MoviesException.class, () -> {
            movies.movie(NON_EXISTENT_ID);
            fail("MovieId should not exists in the database");
        });
        assertEquals(Movies.MOVIE_ID_DOES_NOT_EXISTS, e.getMessage());
    }

    private Long registerUserJose(Movies movies) {
        return movies.addNewUser(1L, JOSEUSER_USERNAME);
    }

    private Long registerUserAntonio(Movies movies) {
        return movies.addNewUser(2L, ANTONIOUSER_USERNAME);
    }

    private Long registerAUser(Movies movies) {
        return movies.addNewUser(3L, "username");
    }

    @AfterEach
    public void tearDown() {
        emf.close();
    }

}
