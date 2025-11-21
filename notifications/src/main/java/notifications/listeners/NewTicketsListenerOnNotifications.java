package notifications.listeners;

import jakarta.persistence.EntityManager;
import notifications.impl.EmailNotificationInfo;
import notifications.impl.NotificationJobInsertStmt;
import publisher.api.EventListener;
import publisher.api.data.shows.TicketsSoldEvent;

import java.sql.Connection;

public class NewTicketsListenerOnNotifications implements EventListener<TicketsSoldEvent> {
    @Override
    public void update(EntityManager em, TicketsSoldEvent info) {
        em.<Connection>runWithConnection(conn -> {
            var emailJobInfo = EmailNotificationInfo.from(info).toJson();
            new NotificationJobInsertStmt().insertJobStmt(conn, emailJobInfo);
        });
    }
}
