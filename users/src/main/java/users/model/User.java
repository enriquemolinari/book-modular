package users.model;

import jakarta.persistence.*;
import lombok.*;
import users.api.UserProfile;
import users.api.UsersException;

import java.util.Map;

import static users.model.Schema.DATABASE_SCHEMA_NAME;

@Entity
@Table(name = "ClientUser", schema = DATABASE_SCHEMA_NAME)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PRIVATE)
@Getter(value = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"userName"})
public class User {

    static final String INVALID_USERNAME = "A valid username must be provided";
    static final String CAN_NOT_CHANGE_PASSWORD = "Some of the provided information is not valid to change the password";
    static final String POINTS_MUST_BE_GREATER_THAN_ZERO = "Points must be greater than zero";
    static final String PASSWORDS_MUST_BE_EQUALS = "Passwords must be equals";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    private String userName;
    private String name;
    private String surname;
    @Embedded
    private Email email;
    // password must not escape by any means out of this object
    @Embedded
    private Password password;
    private int points;

    public User(String name, String surname, String email, String userName, String password,
                String repeatPassword) {
        checkPasswordsMatch(password, repeatPassword);
        this.name = name;
        this.surname = surname;
        this.email = new Email(email);
        this.userName = new NotBlankString(userName,
                INVALID_USERNAME).value();
        this.password = new Password(password);
        this.points = 0;
    }

    private void checkPasswordsMatch(String password, String repeatPassword) {
        if (!password.equals(repeatPassword)) {
            throw new UsersException(PASSWORDS_MUST_BE_EQUALS);
        }
    }

    boolean hasPassword(String password) {
        return this.password.equals(new Password(password));
    }

    public void changePassword(String currentPassword, String newPassword1,
                               String newPassword2) {
        if (!hasPassword(currentPassword)) {
            throw new UsersException(CAN_NOT_CHANGE_PASSWORD);
        }
        checkPasswordsMatch(newPassword2, newPassword1);

        this.password = new Password(newPassword1);
    }

    void newEarnedPoints(int points) {
        if (points <= 0) {
            throw new UsersException(POINTS_MUST_BE_GREATER_THAN_ZERO);
        }
        this.points += points;
    }

    public boolean hasPoints(int points) {
        return this.points == points;
    }

    public String userName() {
        return userName;
    }

    public boolean hasName(String aName) {
        return this.name.equals(aName);
    }

    public boolean hasSurname(String aSurname) {
        return this.surname.equals(aSurname);
    }

    public boolean hasUsername(String aUserName) {
        return this.userName.equals(aUserName);
    }

    String email() {
        return this.email.asString();
    }

    public Map<String, Object> toMap() {
        return Map.of("id", this.id);
    }

    Long id() {
        return id;
    }

    public UserProfile toProfile() {
        return new UserProfile(this.fullName(), this.userName,
                this.email.asString(), this.points);
    }

    private String fullName() {
        return this.name + " " + this.surname;
    }
}
