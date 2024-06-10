package events.api;


import events.api.data.Event;
import events.impl.EventPublisher;
import jakarta.persistence.EntityManager;

public interface Publisher {
    static Publisher creates() {
        return new EventPublisher();
    }

    <E extends Event> void subscribe(EventListener<E> eventListener);

    <E extends Event> void notify(EntityManager em, E event);
}
