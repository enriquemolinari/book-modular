package users.main;

import jakarta.persistence.Persistence;
import users.api.UsersSubSystem;
import users.model.PasetoToken;
import users.model.Users;

public class UsersSubSystemStartUp {
    public static final String DERBY_CLIENT_USERS_MODULE = "derby-client-users-module";
    private static final String SECRET = "nXXh3Xjr2T0ofFilg3kw8BwDEyHmS6OIe4cjWUm2Sm0=";

    public static UsersSubSystem provider() {
        var emf = Persistence
                .createEntityManagerFactory(DERBY_CLIENT_USERS_MODULE);
        new SetUpDb(emf).createSchemaAndPopulateSampleData();
        return new Users(emf, new PasetoToken(SECRET));
    }
}
