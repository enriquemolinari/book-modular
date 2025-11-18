package users.builder;

import common.constants.Environment;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import publisher.api.Event;
import publisher.api.EventListener;
import publisher.api.Publisher;
import users.api.UsersSubSystem;
import users.model.PasetoToken;
import users.model.Users;

public class UsersSubSystemBuilder {
    public static final String DB_NAME = "cinema";
    public static final String HOST = "locahost";
    public static final String PORT = "1527";
    private static final String SECRET = "nXXh3Xjr2T0ofFilg3kw8BwDEyHmS6OIe4cjWUm2Sm0=";
    private final Publisher publisher;
    private String environemnt;

    public UsersSubSystemBuilder() {
        this.publisher = Publisher.creates();
    }

    public UsersSubSystemBuilder testEnv() {
        this.environemnt = Environment.ENVIRONMENT_TEST;
        return this;
    }

    public UsersSubSystemBuilder prodEnv() {
        this.environemnt = Environment.ENVIRONMENT_PROD;
        return this;
    }

    public <E extends Event> UsersSubSystemBuilder addListener(EventListener<E> observer) {
        this.publisher.subscribe(observer);
        return this;
    }

    public UsersSubSystem build() {
        if (isProd()) {
            var emf = new EmfBuilder().clientAndServer(DB_NAME, HOST, PORT).build();
            return createUsersSubSystem(emf);
        }
        var emf = new EmfBuilder().memory().withDropAndCreateDDL().build();
        new SetUpDb(emf).createSchemaAndPopulateSampleData();
        return createUsersSubSystem(emf);
    }

    private boolean isProd() {
        return this.environemnt.equals(Environment.ENVIRONMENT_PROD);
    }

    private EntityManagerFactory createEntityManagerFactory(String persitenceUnitName) {
        return Persistence
                .createEntityManagerFactory(persitenceUnitName);
    }

    private Users createUsersSubSystem(EntityManagerFactory emf) {
        return new Users(emf, new PasetoToken(SECRET), this.publisher);
    }
}
