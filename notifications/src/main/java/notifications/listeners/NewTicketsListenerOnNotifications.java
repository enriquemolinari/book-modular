package notifications.listeners;

import jakarta.persistence.EntityManager;
import notifications.model.EmailNotificationInfo;
import notifications.model.NotificationJobInsertStmt;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import publisher.api.EventListener;
import publisher.api.data.shows.TicketsSoldEvent;

import java.sql.Connection;

public class NewTicketsListenerOnNotifications implements EventListener<TicketsSoldEvent> {
    @Override
    public void update(EntityManager em, TicketsSoldEvent info) {
        Session session = em.unwrap(Session.class);
        session.doWork(new Work() {
            @Override
            public void execute(Connection conn) {
                var emailJobInfo = EmailNotificationInfo.from(info).toJson();
                new NotificationJobInsertStmt().insertJobStmt(conn, emailJobInfo);
            }
        });
    }
}
