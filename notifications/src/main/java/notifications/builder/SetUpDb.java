package notifications.builder;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import notifications.model.User;

class SetUpDb {
    private final EntityManagerFactory emf;

    public SetUpDb(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void createSchemaAndPopulateSampleData() {
        var em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            var eu = new User(1L, "emolinari",
                    "enrique.molinari@gmail.com"
            );
            var nu = new User(2L, "nico",
                    "nico@mymovies.com"
            );
            var lu = new User(3L, "lucia",
                    "lu@mymovies.com");
            var ju = new User(4L, "jsimini",
                    "jsimini@mymovies.com");
            em.persist(eu);
            em.persist(nu);
            em.persist(lu);
            em.persist(ju);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new RuntimeException(e);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}
