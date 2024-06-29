package publisher.api.data.movies;

import publisher.api.Event;

import java.time.LocalDate;
import java.util.Set;

public record NewMovieEvent(
        Long id,
        String name,
        int duration,
        LocalDate releaseDate,
        Set<String> genres
) implements Event {
}
