package notifications.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.function.Function;

public class JpaSession {
    private final EntityManagerFactory emf;

    public JpaSession(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public <T> T inSession(Function<EntityManager, T> toExecute) {
        return emf.callInTransaction(toExecute);
    }
}
