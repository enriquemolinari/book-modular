package notifications.model;

import java.util.Set;

public class Notifications {
    private final EmailProvider emailProvider;

    public Notifications(EmailProvider emailProvider) {
        this.emailProvider = emailProvider;
    }

    public void sendNewSaleEmailToTheUser(Set<Integer> selectedSeats,
                                          String movieName,
                                          String userName,
                                          String startDateTime,
                                          String userEmail,
                                          float totalAmount) {
        var emailTemplate = new NewSaleEmailTemplate(totalAmount,
                userName, selectedSeats, movieName,
                startDateTime);

        this.emailProvider.send(userEmail, emailTemplate.subject(),
                emailTemplate.body());
    }

}
