package users.model;

import common.strings.NotBlankString;
import jakarta.persistence.Embeddable;
import lombok.*;
import users.api.UsersException;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PRIVATE)
@Getter(value = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"password"})
class Password {
    static final String NOT_VALID_PASSWORD = "Password is not valid";
    private String password;

    public Password(String password) {
        String pwd = new NotBlankString(password, new UsersException(NOT_VALID_PASSWORD)).value();
        if (pwd.length() < 12) {
            throw new UsersException(NOT_VALID_PASSWORD);
        }
        // hash password before assign !
        this.password = password;
    }

}
