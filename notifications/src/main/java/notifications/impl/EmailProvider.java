package notifications.impl;

public interface EmailProvider {
    void send(String to, String subject, String body);
}
