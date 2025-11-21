package notifications.impl;

import jakarta.persistence.EntityManagerFactory;
import notifications.api.NotificationsSubSystem;

import java.util.List;

public class Notifications implements NotificationsSubSystem {
    public static final String USER_DOES_NOT_EXISTS = "User does not exists in the Notifications module";
    private final EntityManagerFactory emf;

    public Notifications(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public String[] userBy(Long id) {
        return emf.callInTransaction(em -> {
            User user = new UserRetriever(em).userRetriever(id);
            return user.toArray();
        });
    }

    @Override
    public List<String[]> allJobs() {
        return emf.callInTransaction(em -> {
            var list = new AllJobsRetriever(em).getAllJobs();
            return list.stream().map(item -> item.toArray()).toList();
        });
    }
}