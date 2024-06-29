package publisher.api.data.users;

import publisher.api.Event;

public record NewUserEvent(Long id, String username,
                           String email) implements Event {
}
