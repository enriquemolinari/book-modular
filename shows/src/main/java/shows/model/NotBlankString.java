package shows.model;

import shows.api.ShowsException;

class NotBlankString {

    private final String value;

    public NotBlankString(String value, String errorMsg) {
        if (value == null || value.isBlank()) {
            throw new ShowsException(errorMsg);
        }
        this.value = value;
    }

    public String value() {
        return value;
    }
}
