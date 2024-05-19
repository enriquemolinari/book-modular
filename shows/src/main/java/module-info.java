import shows.api.ShowsSystem;
import shows.main.ShowsSystemStartUp;

module shows {
    requires jakarta.persistence;
    requires lombok;
    requires org.hibernate.orm.core;
    opens shows.model to org.hibernate.orm.core;

    provides ShowsSystem with ShowsSystemStartUp;
    exports shows.api;
}