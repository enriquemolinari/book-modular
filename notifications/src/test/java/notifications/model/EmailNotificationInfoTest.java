package notifications.model;

import events.api.data.shows.TicketsSoldEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailNotificationInfoTest {
    public static final String MOVIE_NAME = "movie name";
    public static final String SHOW_START_TIME = "10:40";
    public static final float TOTAL = 100f;
    public static final int POINTS_WON = 10;
    public static final List<Integer> PAYED_SEATS = List.of(1, 2);
    private static String JSON = "{\"idUser\":1,\"pointsWon\":10,\"total\":100.0,\"payedSeats\":[1,2],\"movieName\":\"movie name\",\"showStartTime\":\"10:40\"}";

    @Test
    public void fromInfoToJson() {
        var info = EmailNotificationInfo.from(new TicketsSoldEvent(1L,
                POINTS_WON,
                TOTAL,
                PAYED_SEATS,
                MOVIE_NAME,
                SHOW_START_TIME));
        assertEquals(JSON, info.toJson());
    }

    @Test
    public void fromJsonToInfo() {
        var info = EmailNotificationInfo.from(JSON);
        assertEquals(1, info.getIdUser());
        assertEquals(MOVIE_NAME, info.getMovieName());
        assertEquals(TOTAL, info.getTotal());
        assertEquals(PAYED_SEATS, info.getPayedSeats());
        assertEquals(POINTS_WON, info.getPointsWon());
        assertEquals(SHOW_START_TIME, info.getShowStartTime());
    }
}
