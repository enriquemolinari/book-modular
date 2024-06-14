package events.impl;

import events.api.EventListener;
import events.api.data.movies.NewMovieEvent;
import events.api.data.users.NewUserEvent;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventPublisherTest {

    @Test
    public void publisherNotifyCorrectListeners() {
        var eventPublisher = new EventPublisher();
        var eventListener1 = new NewUserEventListener();
        var eventListener2 = new NewMovieEventListener();
        eventPublisher.subscribe(eventListener1);
        eventPublisher.subscribe(eventListener2);
        eventPublisher.notify(null, new NewUserEvent(10L, "testuser"));
        assertFalse(eventListener2.updateInvoked());
        assertTrue(eventListener1.updateInvokedWithEvent(new NewUserEvent(10L, "testuser")));
    }
}

class NewUserEventListener extends AbstractEventListener implements EventListener<NewUserEvent> {
    @Override
    public void update(EntityManager em, NewUserEvent info) {
        this.event = info;
    }
}

class NewMovieEventListener extends AbstractEventListener implements EventListener<NewMovieEvent> {

    @Override
    public void update(EntityManager em, NewMovieEvent info) {
        this.event = info;
    }
}