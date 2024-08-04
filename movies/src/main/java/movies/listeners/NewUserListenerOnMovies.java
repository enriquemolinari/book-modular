package movies.listeners;

import jakarta.persistence.EntityManager;
import movies.model.Schema;
import org.hibernate.Session;
import publisher.api.EventListener;
import publisher.api.data.users.NewUserEvent;

import java.sql.PreparedStatement;

import static movies.model.Schema.USER_ENTITY_TABLE_NAME;

public class NewUserListenerOnMovies implements EventListener<NewUserEvent> {
    @Override
    public void update(EntityManager em, NewUserEvent info) {
        Session session = em.unwrap(Session.class);
        session.doWork(conn -> {
            //cannot use EntityManager here, it comes from the Context of another module
            //we don't share entities between modules
            final PreparedStatement st = conn.prepareStatement(
                    "insert into " + Schema.DATABASE_SCHEMA_NAME + "."
                            + USER_ENTITY_TABLE_NAME + " values(?,?)");
            st.setLong(1, info.id());
            st.setString(2, info.username());
            st.executeUpdate();
        });
    }
}

