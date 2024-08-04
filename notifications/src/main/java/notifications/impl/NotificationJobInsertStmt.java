package notifications.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static notifications.impl.Schema.*;

public class NotificationJobInsertStmt {

    public void insertJobStmt(Connection conn, String emailJobInfo) {
        final PreparedStatement st;
        try {
            st = conn.prepareStatement(
                    "insert into " + DATABASE_SCHEMA_NAME + "."
                            + JOBS_ENTITY_TABLE_NAME
                            + "(" + JOBS_JSON_COLUMN_NAME + ","
                            + JOBS_CREATEDAT_COLUMN_NAME + ") values(?, ?)");
            st.setString(1, emailJobInfo);
            st.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
