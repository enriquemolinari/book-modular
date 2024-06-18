package movies.builder;

import common.constants.Environment;
import events.api.Event;
import events.api.EventListener;
import events.api.Publisher;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import movies.api.MoviesSubSystem;
import movies.model.Movies;

import static movies.builder.PersistenceUnit.DERBY_CLIENT_MOVIES_MODULE;
import static movies.builder.PersistenceUnit.DERBY_EMBEDDED_MOVIES_MODULE;

public class MoviesSubSystemBuilder {
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
            var emf = createEntityManagerFactory(DERBY_CLIENT_MOVIES_MODULE);
            return moviesSubsystem(emf);
        }
        var emf = createEntityManagerFactory(DERBY_EMBEDDED_MOVIES_MODULE);
        new SetUpDb(emf).createSchemaAndPopulateSampleData();
        return moviesSubsystem(emf);
    }

    private boolean isProd() {
        return this.environemnt.equals(Environment.ENVIRONMENT_PROD);
    }

    private Movies moviesSubsystem(EntityManagerFactory emf) {
        return new Movies(emf, this.publisher);
    }

    private EntityManagerFactory createEntityManagerFactory(String persistenceUnitName) {
        return Persistence
                .createEntityManagerFactory(persistenceUnitName);
    }
}