package users.main;

import common.constants.Environment;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import users.api.UsersSubSystem;
import users.model.PasetoToken;
import users.model.Users;

import static users.main.PersistenceUnit.DERBY_CLIENT_USERS_MODULE;
import static users.main.PersistenceUnit.DERBY_EMBEDDED_USERS_MODULE;

public class UsersSubSystemStartUp {
    private static final String SECRET = "nXXh3Xjr2T0ofFilg3kw8BwDEyHmS6OIe4cjWUm2Sm0=";

    public static UsersSubSystem provider() {
        String environment = System.getProperty(Environment.ENVIRONMENT_PROPERTY_NAME);

        if (isPROD(environment)) {
            var emf = createEntityManagerFactory(DERBY_CLIENT_USERS_MODULE);
            return new Users(emf, new PasetoToken(SECRET));
        }

        var emf = createEntityManagerFactory(DERBY_EMBEDDED_USERS_MODULE);
        new SetUpDb(emf).createSchemaAndPopulateSampleData();
        return new Users(emf, new PasetoToken(SECRET));
    }

    private static boolean isPROD(String environment) {
        return environment.equals(Environment.ENVIRONMENT_PROD);
    }

    private static EntityManagerFactory createEntityManagerFactory(String persitenceUnitName) {
        return Persistence
                .createEntityManagerFactory(persitenceUnitName);
    }
}
