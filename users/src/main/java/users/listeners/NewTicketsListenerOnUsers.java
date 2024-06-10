package users.listeners;

import events.api.EventListener;
import events.api.data.TicketsSoldEvent;
import jakarta.persistence.EntityManager;

public class NewTicketsListenerOnUsers implements EventListener<TicketsSoldEvent> {
    @Override
    public void update(EntityManager em, TicketsSoldEvent info) {
//        var user = new FindUser().userBy(info.idUser(), em);
//        user.newEarnedPoints(info.pointsWon());
    }
}
