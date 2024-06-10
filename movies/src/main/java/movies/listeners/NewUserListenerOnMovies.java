package movies.listeners;

import events.api.EventListener;
import events.api.data.NewUserEvent;
import jakarta.persistence.EntityManager;
import movies.model.Schema;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static movies.model.Schema.USER_ENTITY_TABLE_NAME;

public class NewUserListenerOnMovies implements EventListener<NewUserEvent> {
    @Override
    public void update(EntityManager em, NewUserEvent info) {
        Session session = em.unwrap(Session.class);
        session.doWork(new Work() {
            @Override
            public void execute(Connection conn) throws SQLException {
                //cannot use EntityManager here, it comes from the Context of another module
                //we don't share entities between modules
                final PreparedStatement st = conn.prepareStatement(
                        "insert into " + Schema.DATABASE_SCHEMA_NAME + "."
                                + USER_ENTITY_TABLE_NAME + " values(?,?)");
                st.setLong(1, info.id());
                st.setString(2, info.username());
                st.executeUpdate();
            }
        });
    }
}

