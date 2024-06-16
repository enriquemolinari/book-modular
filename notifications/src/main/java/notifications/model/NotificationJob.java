package notifications.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static java.lang.String.valueOf;
import static notifications.model.Schema.*;

@Entity
@Table(name = JOBS_ENTITY_TABLE_NAME, schema = DATABASE_SCHEMA_NAME)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PRIVATE)
@Getter(value = AccessLevel.PRIVATE)
public class NotificationJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = JOBS_JSON_COLUMN_NAME)
    private String jsonJob;
    @Column(name = JOBS_CREATEDAT_COLUMN_NAME)
    private LocalDateTime createdAt = LocalDateTime.now();

    public String[] toArray() {
        return new String[]{valueOf(this.id), this.jsonJob, this.createdAt.toString()};
    }
}
