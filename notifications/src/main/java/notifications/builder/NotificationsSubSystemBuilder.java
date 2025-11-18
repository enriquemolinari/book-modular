package notifications.builder;

import common.constants.Environment;
import jakarta.persistence.EntityManagerFactory;
import notifications.api.NotificationsSubSystem;
import notifications.impl.*;

public class NotificationsSubSystemBuilder {
    private static final String DB_NAME = "cinema";
    private static final String HOST = "localhost";
    private static final String DB_PORT = "1527";
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
            var emf = new EmfBuilder()
                    .clientAndServer(DB_NAME, HOST, DB_PORT)
                    .build();
            return notifications(emf);
        }
        var emf = new EmfBuilder()
                .memory()
                .withDropAndCreateDDL()
                .debugQueries()
                .build();
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
}
