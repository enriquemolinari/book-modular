package shows.main;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import shows.api.ShowsSubSystem;
import shows.model.PleasePayPaymentProvider;
import shows.model.Shows;

import static shows.main.PersistenceUnit.DERBY_CLIENT_SHOWS_MODULE;
import static shows.main.PersistenceUnit.DERBY_EMBEDDED_SHOWS_MODULE;

public class ShowsSubSystemStartUp {
    static final String ENVIRONMENT_PROPERTY_NAME = "env";
    static final String ENVIRONMENT_PROD = "prod";

    public static ShowsSubSystem provider() {
        String environment = System.getProperty(ENVIRONMENT_PROPERTY_NAME);
        //TODO: read environment for modules
        //TODO: usar el fake de payment en environment != prod,
        // pero primero me traigo los test del modulo

        // un utils module? constants y utils?
        EntityManagerFactory emf;
        if (environment.equals(ENVIRONMENT_PROD)) {
            emf = Persistence
                    .createEntityManagerFactory(DERBY_CLIENT_SHOWS_MODULE);
        } else {
            emf = Persistence
                    .createEntityManagerFactory(DERBY_EMBEDDED_SHOWS_MODULE);
        }
        new SetUpDb(emf).createSchemaAndPopulateSampleData();
        return new Shows(emf,
                new PleasePayPaymentProvider());
    }
}
