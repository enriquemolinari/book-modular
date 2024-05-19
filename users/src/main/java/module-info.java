import users.api.UsersSubSystem;
import users.main.UsersSubSystemStartUp;

module users {
    requires jakarta.persistence;
    requires lombok;
    requires org.hibernate.orm.core;
    requires dev.paseto.jpaseto.api;
    opens users.model to org.hibernate.orm.core;

    provides UsersSubSystem with UsersSubSystemStartUp;
    exports users.api;
}