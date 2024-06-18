package users.builder;

import common.constants.Environment;
import events.api.Event;
import events.api.EventListener;
import events.api.Publisher;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import users.api.UsersSubSystem;
import users.model.PasetoToken;
import users.model.Users;

import static users.builder.PersistenceUnit.DERBY_CLIENT_USERS_MODULE;
import static users.builder.PersistenceUnit.DERBY_EMBEDDED_USERS_MODULE;

public class UsersSubSystemBuilder {
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
            var emf = createEntityManagerFactory(DERBY_CLIENT_USERS_MODULE);
            return createUsersSubSystem(emf);
        }
        var emf = createEntityManagerFactory(DERBY_EMBEDDED_USERS_MODULE);
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
