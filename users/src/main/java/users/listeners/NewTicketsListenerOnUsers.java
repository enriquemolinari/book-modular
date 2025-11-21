package users.listeners;

import jakarta.persistence.EntityManager;
import publisher.api.EventListener;
import publisher.api.data.shows.TicketsSoldEvent;
import users.model.Schema;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static users.model.Schema.*;

public class NewTicketsListenerOnUsers implements EventListener<TicketsSoldEvent> {
    @Override
    public void update(EntityManager em, TicketsSoldEvent info) {
        em.<Connection>runWithConnection(conn -> {
            //cannot use EntityManager here, it comes from the Context of another module
            //we don't share entities between modules
            final PreparedStatement st = conn.prepareStatement(
                    "update " + DATABASE_SCHEMA_NAME + "."
                            + USER_ENTITY_TABLE_NAME
                            + " set " + Schema.USER_POINTS_COLUMN_NAME + " = " + USER_POINTS_COLUMN_NAME + " + ? "
                            + "where " + USER_ID_COLUMN_NAME + " = ?");
            st.setInt(1, info.pointsWon());
            st.setLong(2, info.idUser());
            st.executeUpdate();
        });
    }
}
