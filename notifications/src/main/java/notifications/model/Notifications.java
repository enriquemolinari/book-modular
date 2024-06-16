package notifications.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import notifications.api.NotificationsSubSystem;

import java.util.List;
import java.util.function.Function;

public class Notifications implements NotificationsSubSystem {
    public static final String USER_DOES_NOT_EXISTS = "User does not exists in the Notifications module";
    private final EntityManagerFactory emf;
    private EntityManager em;

    public Notifications(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public String[] userBy(Long id) {
        return inSession(em -> {
            User user = em.find(User.class, id);
            if (user == null) {
                throw new RuntimeException(USER_DOES_NOT_EXISTS);
            }
            return user.toArray();
        });
    }

    @Override
    public List<String[]> allJobs() {
        return inSession(em -> {
            var query = em.createQuery("from NotificationJob nj order by nj.createdAt",
                    NotificationJob.class);
            return query.getResultList().stream().map(item -> item.toArray()).toList();
        });
    }

    private <T> T inSession(Function<EntityManager, T> toExecute) {
        em = emf.createEntityManager();
        try {
            T t = toExecute.apply(em);
            return t;
        } finally {
            em.close();
        }
    }
}