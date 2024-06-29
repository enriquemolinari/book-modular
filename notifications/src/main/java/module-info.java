module notifications {
    requires publisher;
    requires common;

    requires com.fasterxml.jackson.databind;
    requires jakarta.persistence;
    requires lombok;
    requires org.hibernate.orm.core;
    opens notifications.model to org.hibernate.orm.core, com.fasterxml.jackson.databind;

    exports notifications.api to web;
    exports notifications.listeners to web;
    exports notifications.builder to web;
}