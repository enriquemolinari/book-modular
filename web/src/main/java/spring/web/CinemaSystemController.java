package spring.web;

import movies.api.MovieInfo;
import movies.api.MoviesSubSystem;
import movies.api.UserMovieRate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shows.api.DetailedShowInfo;
import shows.api.MovieShows;
import shows.api.ShowsSubSystem;
import shows.api.Ticket;
import users.api.AuthException;
import users.api.UserProfile;
import users.api.UsersSubSystem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@RestController
public class CinemaSystemController {

    public static final String AUTHENTICATION_REQUIRED = "You must be logged in to perform this action...";
    private static final String TOKEN_COOKIE_NAME = "token";
    private final MoviesSubSystem moviesSubSystem;
    private final ShowsSubSystem showsSubSystem;
    private final UsersSubSystem usersSubSystem;

    public CinemaSystemController(MoviesSubSystem moviesSubSystem,
                                  ShowsSubSystem showsSubSystem,
                                  UsersSubSystem usersSubSystem) {
        this.moviesSubSystem = moviesSubSystem;
        this.showsSubSystem = showsSubSystem;
        this.usersSubSystem = usersSubSystem;
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieInfo> movieDetail(@PathVariable Long id) {
        return ResponseEntity.ok(moviesSubSystem.movie(id));
    }

    @GetMapping("/movies/sorted/rate")
    public ResponseEntity<List<MovieInfo>> moviesSortedByRate(
            @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(moviesSubSystem.pagedMoviesSortedByRate(page));
    }

    @GetMapping("/movies/search/{fullOrPartialName}")
    public ResponseEntity<List<MovieInfo>> moviesSearchBy(
            @PathVariable String fullOrPartialName,
            @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity
                .ok(moviesSubSystem.pagedSearchMovieByName(fullOrPartialName, page));
    }

    @GetMapping("/movies/sorted/releasedate")
    public ResponseEntity<List<MovieInfo>> moviesSortedByReleaseDate(
            @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(moviesSubSystem.pagedMoviesSortedByReleaseDate(page));
    }

    @GetMapping("/movies")
    public ResponseEntity<List<MovieInfo>> allMovies(
            @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(moviesSubSystem.pagedMoviesSortedByName(page));
    }

    @GetMapping("/shows")
    public List<MovieShows> playingTheseDays() {
        return showsSubSystem.showsUntil(LocalDateTime.now().plusDays(10));
    }

    @GetMapping("/shows/{id}")
    public ResponseEntity<DetailedShowInfo> showDetail(
            @PathVariable Long id) {
        return ResponseEntity.ok(showsSubSystem.show(id));
    }

    @PostMapping("/users/register")
    public ResponseEntity<Long> userRegistration(
            @RequestBody UserRegistrationRequest request) {

        return ResponseEntity
                .ok(usersSubSystem.registerUser(request.name(), request.surname(),
                        request.email(),
                        request.username(), request.password(),
                        request.repeatPassword()));
    }

    @GetMapping("/users/profile")
    public ResponseEntity<UserProfile> userProfile(
            @CookieValue(required = false) String token) {

        var profile = ifAuthenticatedDo(token, usersSubSystem::profileFrom);

        return ResponseEntity.ok(profile);
    }

    @PostMapping("/users/changepassword")
    public ResponseEntity<Void> changePassword(
            @CookieValue(required = false) String token,
            @RequestBody ChangePasswordRequest passBody) {

        ifAuthenticatedDo(token, userId -> {
            usersSubSystem.changePassword(userId, passBody.currentPassword(),
                    passBody.newPassword1(), passBody.newPassword2());
            return null;
        });

        return ResponseEntity.ok().build();
    }

    @GetMapping("/movies/{id}/rate")
    public ResponseEntity<List<UserMovieRate>> pagedRatesOfOrderedDate(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(moviesSubSystem.pagedRatesOfOrderedDate(id, page));
    }

    @PostMapping("/login")
    public ResponseEntity<UserProfile> login(@RequestBody LoginRequest form) {
        String token = usersSubSystem.login(form.username(), form.password());
        var profile = usersSubSystem.profileFrom(usersSubSystem.userIdFrom(token));

        var cookie = ResponseCookie.from(TOKEN_COOKIE_NAME, token)
                .httpOnly(true).path("/").build();
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().headers(headers).body(profile);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(required = false) String token) {
        return ifAuthenticatedDo(token, (userId) -> {
            var cookie = ResponseCookie.from(TOKEN_COOKIE_NAME, null)
                    .httpOnly(true).maxAge(0).build();
            var headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
            return ResponseEntity.ok().headers(headers).build();
        });
    }

    @PostMapping("/shows/{showId}/reserve")
    public ResponseEntity<DetailedShowInfo> makeReservation(
            @CookieValue(required = false) String token,
            @PathVariable Long showId, @RequestBody Set<Integer> seats) {

        var showInfo = ifAuthenticatedDo(token,
                userId -> this.showsSubSystem.reserve(userId, showId,
                        seats));

        return ResponseEntity.ok(showInfo);
    }

    @PostMapping("/shows/{showId}/pay")
    public ResponseEntity<Ticket> payment(
            @CookieValue(required = false) String token,
            @PathVariable Long showId, @RequestBody PaymentRequest payment) {

        var ticket = ifAuthenticatedDo(token, userId -> {
            return this.showsSubSystem.pay(userId, showId,
                    payment.selectedSeats(), payment.creditCardNumber(),
                    payment.toYearMonth(),
                    payment.secturityCode());
        });

        return ResponseEntity.ok(ticket);
    }

    @PostMapping("/movies/{movieId}/rate")
    public ResponseEntity<UserMovieRate> rateMovie(
            @CookieValue(required = false) String token,
            @PathVariable Long movieId, @RequestBody RateRequest rateRequest) {

        var userMovieRated = ifAuthenticatedDo(token, userId -> {
            return this.moviesSubSystem.rateMovieBy(userId, movieId,
                    rateRequest.rateValue(), rateRequest.comment());
        });

        return ResponseEntity.ok(userMovieRated);
    }

    private <S> S ifAuthenticatedDo(String token, Function<Long, S> method) {
        var userId = Optional.ofNullable(token).map(this.usersSubSystem::userIdFrom).
                orElseThrow(() -> new AuthException(
                        AUTHENTICATION_REQUIRED));

        return method.apply(userId);
    }
}
