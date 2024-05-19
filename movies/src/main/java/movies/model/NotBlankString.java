package movies.model;

import movies.api.MoviesException;

class NotBlankString {

    private final String value;

    public NotBlankString(String value, String errorMsg) {
        if (value == null || value.isBlank()) {
            throw new MoviesException(errorMsg);
        }
        this.value = value;
    }

    public String value() {
        return value;
    }
}
