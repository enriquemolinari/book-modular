package shows.builder;

import common.constants.Environment;
import jakarta.persistence.EntityManagerFactory;
import publisher.api.Event;
import publisher.api.EventListener;
import publisher.api.Publisher;
import shows.api.ShowsSubSystem;
import shows.model.CreditCardPaymentProvider;
import shows.model.PleasePayPaymentProvider;
import shows.model.Shows;

public class ShowsSubSystemBuilder {
    public static final String DB_NAME = "cinema";
    public static final String HOST = "localhost";
    public static final String PORT = "1527";
    private final Publisher publisher;
    private String environemnt;

    public ShowsSubSystemBuilder() {
        this.publisher = Publisher.creates();
    }

    private CreditCardPaymentProvider doNothingPaymentProvider() {
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
            var emf = new EmfBuilder()
                    .clientAndServer(DB_NAME, HOST, PORT)
                    .build();
            return showsSubsystem(emf, new PleasePayPaymentProvider());
        }
        var emf = new EmfBuilder().memory().withDropAndCreateDDL().build();
        new SetUpDb(emf).createSchemaAndPopulateSampleData();
        return showsSubsystem(emf, doNothingPaymentProvider());
    }

    private boolean isProd() {
        return this.environemnt.equals(Environment.ENVIRONMENT_PROD);
    }

    private Shows showsSubsystem(EntityManagerFactory emf, CreditCardPaymentProvider paymentProvider) {
        return new Shows(emf, paymentProvider, this.publisher);
    }
}
