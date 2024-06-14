package events.api.data.movies;

import events.api.data.Event;

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
