package events.api.data;

public record NewUserEvent(Long id, String username) implements Event {
}
