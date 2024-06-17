package notifications.model;

public class TheBestEmailProvider implements EmailProvider {

    @Override
    public void send(String to, String subject, String body) {
        // implement with your favourite email provider
        // mails sending always succeed
        System.out.println("email send successful to " + to);
    }

}
