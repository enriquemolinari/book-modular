package users.model;

import users.api.UsersException;

class NotBlankString {

    private final String value;

    public NotBlankString(String value, String errorMsg) {
        if (value == null || value.isBlank()) {
            throw new UsersException(errorMsg);
        }
        this.value = value;
    }

    public String value() {
        return value;
    }
}
