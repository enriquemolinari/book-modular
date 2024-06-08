package events.data;

public record NewUserEvent(Long id, String username) implements Event {
}
