package notifications.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.function.Function;

public class JpaSession {
    private EntityManager em;
    private EntityManagerFactory emf;

    public JpaSession(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public <T> T inSession(Function<EntityManager, T> toExecute) {
        em = emf.createEntityManager();
        try {
            T t = toExecute.apply(em);
            return t;
        } finally {
            em.close();
        }
    }
}
