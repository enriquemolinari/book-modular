package events.api.data.users;

import events.api.data.Event;

public record NewUserEvent(Long id, String username) implements Event {
}
