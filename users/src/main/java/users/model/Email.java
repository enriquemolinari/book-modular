package users.model;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import users.api.UsersException;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PRIVATE)
@Getter(value = AccessLevel.PRIVATE)
class Email {

    static final String NOT_VALID_EMAIL = "Email address is not valid";
    private static final String REGEX = "^[\\w-_.+]*[\\w-_.]@(\\w+\\.)+\\w+\\w$";
    private String email;

    public Email(String email) {
        if (!email.matches(REGEX)) {
            throw new UsersException(NOT_VALID_EMAIL);
        }

        this.email = email;
    }

    public String asString() {
        return email;
    }

}
