module notifications {
    requires events;
    requires jakarta.persistence;

    exports notifications.listeners to web;
}