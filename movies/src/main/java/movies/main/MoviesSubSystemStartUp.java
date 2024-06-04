package movies.main;

import common.constants.Environment;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import movies.api.MoviesSubSystem;
import movies.model.Movies;

import static movies.main.PersistenceUnit.DERBY_CLIENT_MOVIES_MODULE;
import static movies.main.PersistenceUnit.DERBY_EMBEDDED_MOVIES_MODULE;

public class MoviesSubSystemStartUp {

    public static MoviesSubSystem provider() {
        String environment = System.getProperty(Environment.ENVIRONMENT_PROPERTY_NAME);

        if (isPROD(environment)) {
            var emf = createEntityManagerFactory(DERBY_CLIENT_MOVIES_MODULE);
            return new Movies(emf);
        }

        var emf = createEntityManagerFactory(DERBY_EMBEDDED_MOVIES_MODULE);
        new SetUpDb(emf).createSchemaAndPopulateSampleData();
        return new Movies(emf);
    }

    private static EntityManagerFactory createEntityManagerFactory(String persitenceUnitName) {
        return Persistence
                .createEntityManagerFactory(persitenceUnitName);
    }

    private static boolean isPROD(String environment) {
        return environment.equals(Environment.ENVIRONMENT_PROD);
    }
}
