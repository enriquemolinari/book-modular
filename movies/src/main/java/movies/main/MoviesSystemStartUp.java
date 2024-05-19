package movies.main;

import jakarta.persistence.Persistence;
import movies.api.MoviesSystem;
import movies.model.Movies;

public class MoviesSystemStartUp {

    public static final String DERBY_CLIENT_MOVIES_MODULE = "derby-client-movies-module";

    public static MoviesSystem provider() {
        var emf = Persistence
                .createEntityManagerFactory(DERBY_CLIENT_MOVIES_MODULE);
        new SetUpDb(emf).createSchemaAndPopulateSampleData();
        return new Movies(emf);
    }
}
