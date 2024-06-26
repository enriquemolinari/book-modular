package publisher.api.data.shows;

import publisher.api.Event;

import java.util.List;

public record TicketsSoldEvent(Long idUser, int pointsWon, float total,
                               List<Integer> payedSeats, String movieName,
                               String showStartTime) implements Event {
}
