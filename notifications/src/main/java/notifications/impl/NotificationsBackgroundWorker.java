package notifications.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificationsBackgroundWorker {
    private final NotificationsJobProcessor jobProcessor;

    public NotificationsBackgroundWorker(NotificationsJobProcessor jobProcessor) {
        this.jobProcessor = jobProcessor;
    }

    public void startUp() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            this.jobProcessor.processAll();
        };
        scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
    }
}
