import movies.api.MoviesSubSystem;
import movies.main.MoviesSubSystemStartUp;

module movies {
    requires common;

    requires jakarta.persistence;
    requires lombok;
    requires org.hibernate.orm.core;
    opens movies.model to org.hibernate.orm.core;

    provides MoviesSubSystem with MoviesSubSystemStartUp;
    exports movies.api;
}