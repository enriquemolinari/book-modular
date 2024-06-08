package users.listeners;

import events.EventListener;
import events.data.TicketsSoldEvent;
import jakarta.persistence.EntityManager;

public class NewTicketsListenerOnUsers implements EventListener<TicketsSoldEvent> {
    @Override
    public void update(EntityManager em, TicketsSoldEvent info) {

    }
}
