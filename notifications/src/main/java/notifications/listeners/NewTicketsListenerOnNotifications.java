package notifications.listeners;

import events.api.EventListener;
import events.api.data.shows.TicketsSoldEvent;
import jakarta.persistence.EntityManager;
import notifications.model.EmailNotificationInfo;
import notifications.model.Schema;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static notifications.model.Schema.*;

public class NewTicketsListenerOnNotifications implements EventListener<TicketsSoldEvent> {
    @Override
    public void update(EntityManager em, TicketsSoldEvent info) {
        Session session = em.unwrap(Session.class);
        session.doWork(new Work() {
            @Override
            public void execute(Connection conn) throws SQLException {
                var emailJobInfo = EmailNotificationInfo.from(info).toJson();
                final PreparedStatement st = conn.prepareStatement(
                        "insert into " + DATABASE_SCHEMA_NAME + "."
                                + JOBS_ENTITY_TABLE_NAME
                                + "(" + JOBS_JSON_COLUMN_NAME + ","
                                + Schema.JOBS_CREATEDAT_COLUMN_NAME + ") values(?, ?)");
                st.setString(1, emailJobInfo);
                st.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                st.executeUpdate();
            }
        });

    }
}
