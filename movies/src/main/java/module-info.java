module movies {
    requires common;
    requires publisher;

    requires jakarta.persistence;
    requires lombok;
    requires org.hibernate.orm.core;
    opens movies.model to org.hibernate.orm.core;

    //even that only web use the module api, its classes might require to be
    //serialized to json by other modules
    //jackson databind requires them to be open or exported
    exports movies.api;
    exports movies.builder to web;
    exports movies.listeners to web;
}