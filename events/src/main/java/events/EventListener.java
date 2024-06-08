package events;

import events.data.Event;
import jakarta.persistence.EntityManager;

public interface EventListener<E extends Event> {
    void update(EntityManager em, E info);
}
