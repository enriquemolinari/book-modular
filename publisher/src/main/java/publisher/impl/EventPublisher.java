package publisher.impl;

import jakarta.persistence.EntityManager;
import publisher.api.Event;
import publisher.api.EventListener;
import publisher.api.Publisher;

import java.util.ArrayList;
import java.util.List;

public class EventPublisher implements Publisher {
    private final List<EventListener<? extends Event>> eventListeners = new ArrayList<>();

    @Override
    public <E extends Event> void subscribe(EventListener<E> eventListener) {
        eventListeners.add(eventListener);
    }

    @Override
    public <E extends Event> void notify(EntityManager em, E event) {
        for (EventListener<? extends Event> eventListener : eventListeners) {
            if (eventListener.getClass().getGenericInterfaces()[0].getTypeName()
                    .contains(event.getClass().getTypeName())) {
                ((EventListener<E>) eventListener).update(em, event);
            }
        }
    }
}
