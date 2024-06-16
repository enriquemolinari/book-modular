package notifications.api;

import java.util.List;

public interface NotificationsSubSystem {
    String[] userBy(Long id);

    List<String[]> allJobs();
}
