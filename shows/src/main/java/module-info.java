module shows {
    requires common;
    requires events;

    requires jakarta.persistence;
    requires lombok;
    requires org.hibernate.orm.core;
    opens shows.model to org.hibernate.orm.core;

    exports shows.api;
    exports shows.builder to web;
    exports shows.listeners to web;
}