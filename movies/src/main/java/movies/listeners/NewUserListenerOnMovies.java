package movies.listeners;

import events.EventListener;
import events.data.NewUserEvent;
import jakarta.persistence.EntityManager;

public class NewUserListenerOnMovies implements EventListener<NewUserEvent> {
    @Override
    public void update(EntityManager em, NewUserEvent info) {

    }
}

