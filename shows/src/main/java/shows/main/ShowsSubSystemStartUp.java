package shows.main;

import common.constants.Environment;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import shows.api.ShowsSubSystem;
import shows.model.CreditCardPaymentProvider;
import shows.model.PleasePayPaymentProvider;
import shows.model.Shows;

import static shows.main.PersistenceUnit.DERBY_CLIENT_SHOWS_MODULE;
import static shows.main.PersistenceUnit.DERBY_EMBEDDED_SHOWS_MODULE;

public class ShowsSubSystemStartUp {

    public static ShowsSubSystem provider() {
        String environment = System.getProperty(Environment.ENVIRONMENT_PROPERTY_NAME);
        if (isPROD(environment)) {
            var emf = createEntityManagerFactory(DERBY_CLIENT_SHOWS_MODULE);
            return showsSubsystem(emf, new PleasePayPaymentProvider());
        }
        var emf = createEntityManagerFactory(DERBY_EMBEDDED_SHOWS_MODULE);
        setUpSampleDatabase(emf);
        return showsSubsystem(emf, doNothingPaymentProvider());
    }

    private static CreditCardPaymentProvider doNothingPaymentProvider() {
        return (creditCardNumber, expire, securityCode, totalAmount) -> {
        };
    }

    private static boolean isPROD(String environment) {
        return environment.equals(Environment.ENVIRONMENT_PROD);
    }

    private static Shows showsSubsystem(EntityManagerFactory emf, CreditCardPaymentProvider paymentProvider) {
        return new Shows(emf, paymentProvider);
    }

    private static void setUpSampleDatabase(EntityManagerFactory emf) {
        new SetUpDb(emf).createSchemaAndPopulateSampleData();
    }

    private static EntityManagerFactory createEntityManagerFactory(String persistenceUnitName) {
        return Persistence
                .createEntityManagerFactory(persistenceUnitName);
    }
}
