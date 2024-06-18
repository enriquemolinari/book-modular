package shows.model;

import events.api.Event;
import events.api.EventListener;
import events.api.Publisher;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class FakePublisher implements Publisher {
    private final List<EventListener> listeners = new ArrayList<>();
    private Event event;

    @Override
    public <E extends Event> void subscribe(EventListener<E> eventListener) {
        this.listeners.add(eventListener);
    }

    @Override
    public <E extends Event> void notify(EntityManager em, E event) {
        this.event = event;
    }

    public <E extends Event> boolean invokedWithEvent(E event) {
        if (event == null) {
            return false;
        }
        return event.equals(this.event);
    }
}
