package shows.listeners;

import events.EventListener;
import events.data.NewUserEvent;
import jakarta.persistence.EntityManager;

public class NewUserListenerOnShows implements EventListener<NewUserEvent> {
    @Override
    public void update(EntityManager em, NewUserEvent info) {

    }
}
