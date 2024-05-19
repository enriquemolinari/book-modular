package shows.main;

import jakarta.persistence.Persistence;
import shows.api.ShowsSystem;
import shows.model.PleasePayPaymentProvider;
import shows.model.Shows;
import shows.model.TheBestEmailProvider;

public class ShowsSystemStartUp {
    public static final String DERBY_CLIENT_MOVIES_MODULE = "derby-client-shows-module";

    public static ShowsSystem provider() {
        var emf = Persistence
                .createEntityManagerFactory(DERBY_CLIENT_MOVIES_MODULE);
        new SetUpDb(emf).createSchemaAndPopulateSampleData();
        return new Shows(emf,
                new PleasePayPaymentProvider(),
                new TheBestEmailProvider());
    }
}
