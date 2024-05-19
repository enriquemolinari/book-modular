package shows.main;

import jakarta.persistence.Persistence;
import shows.api.ShowsSubSystem;
import shows.model.PleasePayPaymentProvider;
import shows.model.Shows;

public class ShowsSubSystemStartUp {
    public static final String DERBY_CLIENT_SHOWS_MODULE = "derby-client-shows-module";

    public static ShowsSubSystem provider() {
        var emf = Persistence
                .createEntityManagerFactory(DERBY_CLIENT_SHOWS_MODULE);
        new SetUpDb(emf).createSchemaAndPopulateSampleData();
        return new Shows(emf,
                new PleasePayPaymentProvider());
    }
}
