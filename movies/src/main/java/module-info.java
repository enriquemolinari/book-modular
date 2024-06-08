module movies {
    requires common;
    requires events;

    requires jakarta.persistence;
    requires lombok;
    requires org.hibernate.orm.core;
    opens movies.model to org.hibernate.orm.core;

//    exports movies.api to web;
    exports movies.api;
    exports movies.builder to web;
    exports movies.listeners to web;
}