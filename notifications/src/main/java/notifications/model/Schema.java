package notifications.model;

public interface Schema {
    String DATABASE_SCHEMA_NAME = "notifications";
    String USER_ENTITY_TABLE_NAME = "clientuser";
    String JOBS_ENTITY_TABLE_NAME = "queue";
    String USER_ID_COLUMN_NAME = "id";
    String USER_USERNAME_COLUMN_NAME = "username";
    String USER_EMAIL_COLUMN_NAME = "email";
    String JOBS_JSON_COLUMN_NAME = "jsonjob";
    String JOBS_CREATEDAT_COLUMN_NAME = "createdat";
}
