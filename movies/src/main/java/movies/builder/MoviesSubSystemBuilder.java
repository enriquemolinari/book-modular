package movies.builder;

import common.constants.Environment;
import jakarta.persistence.EntityManagerFactory;
import movies.api.MoviesSubSystem;
import movies.model.Movies;
import publisher.api.Event;
import publisher.api.EventListener;
import publisher.api.Publisher;

public class MoviesSubSystemBuilder {
    public static final String HOST = "localhost";
    public static final String DB_PORT = "1527";
    public static final String DB_NAME = "cinema";
    private final Publisher publisher;
    private String environemnt;

    public MoviesSubSystemBuilder() {
        this.publisher = Publisher.creates();
    }

    public MoviesSubSystemBuilder testEnv() {
        this.environemnt = Environment.ENVIRONMENT_TEST;
        return this;
    }

    public <E extends Event> MoviesSubSystemBuilder addListener(EventListener<E> listener) {
        this.publisher.subscribe(listener);
        return this;
    }

    public MoviesSubSystemBuilder prodEnv() {
        this.environemnt = Environment.ENVIRONMENT_PROD;
        return this;
    }

    public MoviesSubSystem build() {
        if (isProd()) {
            var emf = new EmfBuilder()
                    .clientAndServer(DB_NAME, HOST, DB_PORT)
                    .build();
            return moviesSubsystem(emf);
        }
        var emf = new EmfBuilder()
                .memory()
                .withDropAndCreateDDL()
                .debugQueries()
                .build();
        new SetUpDb(emf).createSchemaAndPopulateSampleData();
        return moviesSubsystem(emf);
    }

    private boolean isProd() {
        return this.environemnt.equals(Environment.ENVIRONMENT_PROD);
    }

    private Movies moviesSubsystem(EntityManagerFactory emf) {
        return new Movies(emf, this.publisher);
    }

}