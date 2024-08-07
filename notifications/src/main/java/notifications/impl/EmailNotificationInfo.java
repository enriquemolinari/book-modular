package notifications.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import publisher.api.data.shows.TicketsSoldEvent;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EmailNotificationInfo {
    private Long idUser;
    private int pointsWon;
    private float total;
    private List<Integer> payedSeats;
    private String movieName;
    private String showStartTime;

    private EmailNotificationInfo(Long idUser, int pointsWon, float total, List<Integer> payedSeats, String movieName, String showStartTime) {
        this.idUser = idUser;
        this.pointsWon = pointsWon;
        this.total = total;
        this.payedSeats = payedSeats;
        this.movieName = movieName;
        this.showStartTime = showStartTime;
    }

    public static EmailNotificationInfo from(TicketsSoldEvent ticketsSoldEvent) {
        return new EmailNotificationInfo(ticketsSoldEvent.idUser(),
                ticketsSoldEvent.pointsWon(),
                ticketsSoldEvent.total(),
                ticketsSoldEvent.payedSeats(),
                ticketsSoldEvent.movieName(),
                ticketsSoldEvent.showStartTime());
    }

    public static EmailNotificationInfo from(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, EmailNotificationInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
