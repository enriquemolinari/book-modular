module shows {
    requires common;
    requires publisher;

    requires jakarta.persistence;
    requires lombok;
    requires org.hibernate.orm.core;
    requires java.sql;
    opens shows.model to org.hibernate.orm.core;

    exports shows.api;
    exports shows.builder to web;
    exports shows.listeners to web;
}