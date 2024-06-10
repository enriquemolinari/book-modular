package shows.listeners;

import events.api.EventListener;
import events.api.data.NewMovieEvent;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import shows.model.Schema;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static shows.model.Schema.*;

public class NewMovieListenerOnShows implements EventListener<NewMovieEvent> {
    @Override
    public void update(EntityManager em, NewMovieEvent info) {
        Session session = em.unwrap(Session.class);
        session.doWork(new Work() {
            @Override
            public void execute(Connection conn) throws SQLException {
                //cannot use EntityManager here, it comes from the Context of another module
                //we don't share entityes between modules
                String insertStmt = "insert into " + DATABASE_SCHEMA_NAME + "."
                        + MOVIE_ENTITY_TABLE_NAME
                        + "(" + MOVIE_ID_COLUMN_NAME
                        + ", " + MOVIE_NAME_COLUMN_NAME
                        + ", " + MOVIE_DURATION_COLUMN_NAME
                        + ", " + MOVIE_RELEASEDATE_COLUMN_NAME + ") "
                        + "values(?,?,?,?)";
                final PreparedStatement movieStm = conn.prepareStatement(insertStmt);
                movieStm.setLong(1, info.id());
                movieStm.setString(2, info.name());
                movieStm.setInt(3, info.duration());
                movieStm.setDate(4, Date.valueOf(info.releaseDate()));
                movieStm.executeUpdate();

                //genres
                final PreparedStatement genreStm = conn.prepareStatement(
                        "insert into " + Schema.DATABASE_SCHEMA_NAME + "."
                                + MOVIE_GENRES_TABLE_NAME + "(" + MOVIE_GENRE_ID_COLUMN_NAME
                                + ", " + MOVIE_GENRE_NAME_COLUMN_NAME
                                + ") values(?,?)");
                for (String genre : info.genres()) {
                    genreStm.setLong(1, info.id());
                    genreStm.setString(2, genre);
                    genreStm.addBatch();
                }
                genreStm.executeBatch();
            }
        });
    }
}

