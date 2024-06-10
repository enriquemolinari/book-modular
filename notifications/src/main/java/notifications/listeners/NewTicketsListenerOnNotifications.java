package notifications.listeners;

import events.api.EventListener;
import events.api.data.TicketsSoldEvent;
import jakarta.persistence.EntityManager;

public class NewTicketsListenerOnNotifications implements EventListener<TicketsSoldEvent> {
    @Override
    public void update(EntityManager em, TicketsSoldEvent info) {

    }
}
