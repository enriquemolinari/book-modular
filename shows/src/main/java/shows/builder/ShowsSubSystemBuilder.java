package shows.builder;

import common.constants.Environment;
import events.api.Event;
import events.api.EventListener;
import events.api.Publisher;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import shows.api.ShowsSubSystem;
import shows.model.CreditCardPaymentProvider;
import shows.model.PleasePayPaymentProvider;
import shows.model.Shows;

import static shows.builder.PersistenceUnit.DERBY_CLIENT_SHOWS_MODULE;
import static shows.builder.PersistenceUnit.DERBY_EMBEDDED_SHOWS_MODULE;

public class ShowsSubSystemBuilder {
    private final Publisher publisher;
    private String environemnt;

    public ShowsSubSystemBuilder() {
        this.publisher = Publisher.creates();
    }

    private static CreditCardPaymentProvider doNothingPaymentProvider() {
        return (creditCardNumber, expire, securityCode, totalAmount) -> {
        };
    }

    public <E extends Event> ShowsSubSystemBuilder addListener(EventListener<E> listener) {
        this.publisher.subscribe(listener);
        return this;
    }

    public ShowsSubSystemBuilder testEnv() {
        this.environemnt = Environment.ENVIRONMENT_TEST;
        return this;
    }

    public ShowsSubSystemBuilder prodEnv() {
        this.environemnt = Environment.ENVIRONMENT_PROD;
        return this;
    }

    public ShowsSubSystem build() {
        if (isProd()) {
            var emf = createEntityManagerFactory(DERBY_CLIENT_SHOWS_MODULE);
            return showsSubsystem(emf, new PleasePayPaymentProvider());
        }
        var emf = createEntityManagerFactory(DERBY_EMBEDDED_SHOWS_MODULE);
        new SetUpDb(emf).createSchemaAndPopulateSampleData();
        return showsSubsystem(emf, doNothingPaymentProvider());
    }

    private boolean isProd() {
        return this.environemnt.equals(Environment.ENVIRONMENT_PROD);
    }

    private Shows showsSubsystem(EntityManagerFactory emf, CreditCardPaymentProvider paymentProvider) {
        return new Shows(emf, paymentProvider, this.publisher);
    }

    private EntityManagerFactory createEntityManagerFactory(String persistenceUnitName) {
        return Persistence
                .createEntityManagerFactory(persistenceUnitName);
    }
}
