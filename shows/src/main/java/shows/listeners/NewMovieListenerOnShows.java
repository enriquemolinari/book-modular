package shows.listeners;

import events.EventListener;
import events.data.NewMovieEvent;
import jakarta.persistence.EntityManager;

public class NewMovieListenerOnShows implements EventListener<NewMovieEvent> {
    @Override
    public void update(EntityManager em, NewMovieEvent info) {

    }
}

