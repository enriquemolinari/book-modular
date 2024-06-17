package notifications.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import static java.lang.String.valueOf;
import static notifications.model.Schema.*;

@Entity
@Table(name = USER_ENTITY_TABLE_NAME, schema = DATABASE_SCHEMA_NAME)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PRIVATE)
@Getter(value = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"userName"})
public class User {

    @Id
    @Column(name = USER_ID_COLUMN_NAME)
    private long id;
    @Column(unique = true, name = USER_USERNAME_COLUMN_NAME)
    private String userName;
    @Column(unique = true, name = USER_EMAIL_COLUMN_NAME)
    private String email;

    public User(long id, String userName, String email) {
        this.id = id;
        this.userName = userName;
        this.email = email;
    }

    String username() {
        return this.userName;
    }

    String email() {
        return this.email;
    }

    public String[] toArray() {
        return new String[]{valueOf(this.id), this.userName, this.email};
    }
}
