package notifications.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.function.Consumer;

public class JpaTx {
    private final EntityManagerFactory emf;

    public JpaTx(EntityManagerFactory emf) {
        this.emf = emf;
    }

    void inTx(Consumer<EntityManager> toExecute) {
        EntityManager em = this.emf.createEntityManager();
        var tx = em.getTransaction();

        try {
            tx.begin();

            toExecute.accept(em);
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
