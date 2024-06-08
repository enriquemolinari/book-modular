module users {
    requires common;
    requires events;

    requires jakarta.persistence;
    requires lombok;
    requires org.hibernate.orm.core;
    requires dev.paseto.jpaseto.api;
    opens users.model to org.hibernate.orm.core;

    exports users.api;
    exports users.builder to web;
    exports users.listeners to web;
}