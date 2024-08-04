package notifications.listeners;

import jakarta.persistence.EntityManager;
import notifications.model.EmailNotificationInfo;
import notifications.model.NotificationJobInsertStmt;
import org.hibernate.Session;
import publisher.api.EventListener;
import publisher.api.data.shows.TicketsSoldEvent;

public class NewTicketsListenerOnNotifications implements EventListener<TicketsSoldEvent> {
    @Override
    public void update(EntityManager em, TicketsSoldEvent info) {
        Session session = em.unwrap(Session.class);
        session.doWork(conn -> {
            var emailJobInfo = EmailNotificationInfo.from(info).toJson();
            new NotificationJobInsertStmt().insertJobStmt(conn, emailJobInfo);
        });
    }
}
