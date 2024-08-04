package notifications.listeners;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import publisher.api.EventListener;
import publisher.api.data.users.NewUserEvent;

import java.sql.PreparedStatement;

import static notifications.model.Schema.*;

public class NewUserListenerOnNotifications implements EventListener<NewUserEvent> {
    @Override
    public void update(EntityManager em, NewUserEvent info) {
        Session session = em.unwrap(Session.class);
        session.doWork(conn -> {
            final PreparedStatement st = conn.prepareStatement(
                    "insert into " + DATABASE_SCHEMA_NAME + "."
                            + USER_ENTITY_TABLE_NAME
                            + "(" + USER_ID_COLUMN_NAME + "," + USER_USERNAME_COLUMN_NAME
                            + "," + USER_EMAIL_COLUMN_NAME + ") values(?, ?, ?)");
            st.setLong(1, info.id());
            st.setString(2, info.username());
            st.setString(3, info.email());
            st.executeUpdate();
        });
    }
}
