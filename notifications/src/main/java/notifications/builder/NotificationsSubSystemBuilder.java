package notifications.builder;

import common.constants.Environment;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import notifications.api.NotificationsSubSystem;
import notifications.model.Notifications;
import notifications.model.NotificationsBackgroundWorker;

import static notifications.builder.PersistenceUnit.DERBY_CLIENT_NOTIFICATIONS_MODULE;
import static notifications.builder.PersistenceUnit.DERBY_EMBEDDED_NOTIFICATIONS_MODULE;

public class NotificationsSubSystemBuilder {
    private String environemnt;
    private boolean startBackgroundJob;

    public NotificationsSubSystemBuilder testEnv() {
        this.environemnt = Environment.ENVIRONMENT_TEST;
        return this;
    }

    public NotificationsSubSystemBuilder prodEnv() {
        this.environemnt = Environment.ENVIRONMENT_PROD;
        return this;
    }

    public NotificationsSubSystemBuilder startBackgroundJob() {
        this.startBackgroundJob = true;
        return this;
    }

    public NotificationsSubSystem build() {
        if (isProd()) {
            var emf = createEntityManagerFactory(DERBY_CLIENT_NOTIFICATIONS_MODULE);
            shouldStartBackgroundJob();
            return new Notifications(emf);
        }
        var emf = createEntityManagerFactory(DERBY_EMBEDDED_NOTIFICATIONS_MODULE);
        new SetUpDb(emf).createSchemaAndPopulateSampleData();
        shouldStartBackgroundJob();
        return new Notifications(emf);
    }

    private void shouldStartBackgroundJob() {
        if (startBackgroundJob) {
            new NotificationsBackgroundWorker().startUp();
        }
    }

    private boolean isProd() {
        return this.environemnt.equals(Environment.ENVIRONMENT_PROD);
    }

    private EntityManagerFactory createEntityManagerFactory(String persistenceUnitName) {
        return Persistence
                .createEntityManagerFactory(persistenceUnitName);
    }
}
