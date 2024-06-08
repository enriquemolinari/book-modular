package shows.model;

import common.strings.NotBlankString;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shows.api.MovieShows;
import shows.api.ShowsException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static shows.model.Schema.DATABASE_SCHEMA_NAME;

@Entity
@Table(schema = DATABASE_SCHEMA_NAME)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PRIVATE)
@Getter(value = AccessLevel.PRIVATE)
public class Movie {
    static final String MOVIE_PLOT_INVALID = "Movie plot must not be null or blank";
    static final String MOVIE_NAME_INVALID = "Movie name must not be null or blank";
    static final String DURATION_INVALID = "Movie's duration must be greater than 0";
    static final String GENRES_INVALID = "You must add at least one genre to the movie";
    @Id
    private long id;
    private String name;
    private int duration;
    private LocalDate releaseDate;
    @ElementCollection
    @CollectionTable(schema = DATABASE_SCHEMA_NAME)
    private Set<String> genres;
    @OneToMany(mappedBy = "movieToBeScreened")
    private List<ShowTime> showTimes;

    public Movie(long id, String name, int duration, LocalDate releaseDate,
                 Set<String> genres) {
        this.id = id;
        checkDurationGreaterThanZero(duration);
        checkGenresAtLeastHasOne(genres);
        this.name = new NotBlankString(name, new ShowsException(MOVIE_NAME_INVALID)).value();
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.genres = genres;
    }

    private void checkGenresAtLeastHasOne(Set<String> genres) {
        if (genres.isEmpty()) {
            throw new ShowsException(GENRES_INVALID);
        }
    }

    private void checkDurationGreaterThanZero(int duration) {
        if (duration <= 0) {
            throw new ShowsException(DURATION_INVALID);
        }
    }

    public boolean hasDurationOf(int aDuration) {
        return this.duration == aDuration;
    }

    public boolean isNamed(String aName) {
        return this.name.equals(aName);
    }


    String name() {
        return this.name;
    }

    public MovieShows toMovieShow() {
        return new MovieShows(this.id, this.name,
                new MovieDurationFormat(duration).toString(),
                genres(), this.showTimes.stream()
                .map(ShowTime::toShowInfo).toList());
    }

    private Set<String> genres() {
        return this.genres.stream().map(g -> capitalizeFirstLetter(g))
                .collect(Collectors.toSet());
    }

    private String capitalizeFirstLetter(String aString) {
        return aString.substring(0, 1).toUpperCase()
                + aString.substring(1).toLowerCase();
    }

    int duration() {
        return this.duration;
    }

    LocalDateTime releaseDateAsDateTime() {
        return this.releaseDate.atTime(0, 0);
    }
}
