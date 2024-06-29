package publisher.api;


import jakarta.persistence.EntityManager;
import publisher.impl.EventPublisher;

public interface Publisher {
    static Publisher creates() {
        return new EventPublisher();
    }

    <E extends Event> void subscribe(EventListener<E> eventListener);

    <E extends Event> void notify(EntityManager em, E event);
}
