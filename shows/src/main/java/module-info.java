import shows.api.ShowsSubSystem;
import shows.main.ShowsSubSystemStartUp;

module shows {
    requires jakarta.persistence;
    requires lombok;
    requires org.hibernate.orm.core;
    opens shows.model to org.hibernate.orm.core;

    provides ShowsSubSystem with ShowsSubSystemStartUp;
    exports shows.api;
}