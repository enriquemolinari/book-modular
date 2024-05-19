package spring.web;

import movies.api.MovieInfo;
import movies.api.MoviesSystem;
import movies.api.UserMovieRate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shows.api.DetailedShowInfo;
import shows.api.MovieShows;
import shows.api.ShowsSystem;
import shows.api.Ticket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@RestController
public class CinemaSystemController {

    public static final String AUTHENTICATION_REQUIRED = "You must be logged in to perform this action...";
    private static final String TOKEN_COOKIE_NAME = "token";
    private final MoviesSystem moviesSystem;
    private final ShowsSystem showsSystem;

    public CinemaSystemController(MoviesSystem moviesSystem, ShowsSystem showsSystem) {
        this.moviesSystem = moviesSystem;
        this.showsSystem = showsSystem;
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieInfo> movieDetail(@PathVariable Long id) {
        return ResponseEntity.ok(moviesSystem.movie(id));
    }

    @GetMapping("/movies/sorted/rate")
    public ResponseEntity<List<MovieInfo>> moviesSortedByRate(
            @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(moviesSystem.pagedMoviesSortedByRate(page));
    }

    @GetMapping("/movies/search/{fullOrPartialName}")
    public ResponseEntity<List<MovieInfo>> moviesSearchBy(
            @PathVariable String fullOrPartialName,
            @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity
                .ok(moviesSystem.pagedSearchMovieByName(fullOrPartialName, page));
    }

    @GetMapping("/movies/sorted/releasedate")
    public ResponseEntity<List<MovieInfo>> moviesSortedByReleaseDate(
            @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(moviesSystem.pagedMoviesSortedByReleaseDate(page));
    }

    @GetMapping("/movies")
    public ResponseEntity<List<MovieInfo>> allMovies(
            @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(moviesSystem.pagedMoviesSortedByName(page));
    }

    @GetMapping("/shows")
    public List<MovieShows> playingTheseDays() {
        return showsSystem.showsUntil(LocalDateTime.now().plusDays(10));
    }

    @GetMapping("/shows/{id}")
    public ResponseEntity<DetailedShowInfo> showDetail(
            @PathVariable Long id) {
        return ResponseEntity.ok(showsSystem.show(id));
    }

//    @PostMapping("/users/register")
//    public ResponseEntity<Long> userRegistration(
//            @RequestBody UserRegistrationRequest request) {
//
//        return ResponseEntity
//                .ok(moviesSystem.registerUser(request.name(), request.surname(),
//                        request.email(),
//                        request.username(), request.password(),
//                        request.repeatPassword()));
//    }
//
//    @GetMapping("/users/profile")
//    public ResponseEntity<UserProfile> userProfile(
//            @CookieValue(required = false) String token) {
//
//        var profile = ifAuthenticatedDo(token, moviesSystem::profileFrom);
//
//        return ResponseEntity.ok(profile);
//    }
//
//    @PostMapping("/users/changepassword")
//    public ResponseEntity<Void> changePassword(
//            @CookieValue(required = false) String token,
//            @RequestBody ChangePasswordRequest passBody) {
//
//        ifAuthenticatedDo(token, userId -> {
//            moviesSystem.changePassword(userId, passBody.currentPassword(),
//                    passBody.newPassword1(), passBody.newPassword2());
//            return null;
//        });
//
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/movies/{id}/rate")
    public ResponseEntity<List<UserMovieRate>> pagedRatesOfOrderedDate(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(moviesSystem.pagedRatesOfOrderedDate(id, page));
    }

//    @PostMapping("/login")
//    public ResponseEntity<UserProfile> login(@RequestBody LoginRequest form) {
//        String token = moviesSystem.login(form.username(), form.password());
//        var profile = moviesSystem.profileFrom(moviesSystem.userIdFrom(token));
//
//        var cookie = ResponseCookie.from(TOKEN_COOKIE_NAME, token)
//                .httpOnly(true).path("/").build();
//        var headers = new HttpHeaders();
//        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
//        return ResponseEntity.ok().headers(headers).body(profile);
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<Void> logout(
//            @CookieValue(required = false) String token) {
//        return ifAuthenticatedDo(token, (userId) -> {
//            var cookie = ResponseCookie.from(TOKEN_COOKIE_NAME, null)
//                    .httpOnly(true).maxAge(0).build();
//            var headers = new HttpHeaders();
//            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
//            return ResponseEntity.ok().headers(headers).build();
//        });
//    }

    @PostMapping("/shows/{showId}/reserve")
    public ResponseEntity<DetailedShowInfo> makeReservation(
            @CookieValue(required = false) String token,
            @PathVariable Long showId, @RequestBody Set<Integer> seats) {

        var showInfo = ifAuthenticatedDo(token,
                userId -> this.showsSystem.reserve(userId, showId,
                        seats));

        return ResponseEntity.ok(showInfo);
    }

    @PostMapping("/shows/{showId}/pay")
    public ResponseEntity<Ticket> payment(
            @CookieValue(required = false) String token,
            @PathVariable Long showId, @RequestBody PaymentRequest payment) {

        var ticket = ifAuthenticatedDo(token, userId -> {
            return this.showsSystem.pay(userId, showId,
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
            return this.moviesSystem.rateMovieBy(userId, movieId,
                    rateRequest.rateValue(), rateRequest.comment());
        });

        return ResponseEntity.ok(userMovieRated);
    }

    private <S> S ifAuthenticatedDo(String token, Function<Long, S> method) {
//        var userId = Optional.ofNullable(token).map(this.moviesSystem::userIdFrom).
//                orElseThrow(() -> new AuthException(
//                        AUTHENTICATION_REQUIRED));
//
//        return method.apply(userId);
        return null;
    }
}
