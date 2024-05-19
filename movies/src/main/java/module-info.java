import movies.api.MoviesSystem;
import movies.main.MoviesSystemStartUp;

module movies {
    requires jakarta.persistence;
    requires lombok;
    requires org.hibernate.orm.core;
    opens movies.model to org.hibernate.orm.core;

    provides MoviesSystem with MoviesSystemStartUp;
    exports movies.api;
}