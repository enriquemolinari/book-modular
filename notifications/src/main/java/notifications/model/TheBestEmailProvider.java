package notifications.model;

public class TheBestEmailProvider implements EmailProvider {

    @Override
    public void send(String to, String subject, String body) {
        // mails sending always succeed
    }

}
