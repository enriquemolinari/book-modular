package users.api;

public class AuthException extends RuntimeException {
    public AuthException(String msg) {
        super(msg);
    }
}
