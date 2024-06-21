package notifications.builder;

import common.constants.Environment;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import notifications.api.NotificationsSubSystem;
import notifications.model.*;

import static notifications.model.PersistenceUnit.DERBY_CLIENT_NOTIFICATIONS_MODULE;
import static notifications.model.PersistenceUnit.DERBY_EMBEDDED_NOTIFICATIONS_MODULE;

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
            return notifications(emf);
        }
        var emf = createEntityManagerFactory(DERBY_EMBEDDED_NOTIFICATIONS_MODULE);
        new SetUpDb(emf).createSchemaAndPopulateSampleData();
        return notifications(emf);
    }

    private Notifications notifications(EntityManagerFactory emf) {
        var notifications = new Notifications(emf);
        shouldStartBackgroundJob(emf);
        return notifications;
    }

    private void shouldStartBackgroundJob(EntityManagerFactory emf) {
        if (startBackgroundJob) {
            new NotificationsBackgroundWorker(
                    new NotificationsJobProcessor(emf,
                            new NotificationSender(new TheBestEmailProvider()))).startUp();
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
