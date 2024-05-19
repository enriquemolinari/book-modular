package users.main;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import users.model.User;

public class SetUpDb {

    private final EntityManagerFactory emf;

    public SetUpDb(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void createSchemaAndPopulateSampleData() {
        var em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            var eu = new User("Enrique",
                    "Molinari",
                    "enrique.molinari@gmail.com",
                    "emolinari",
                    "123456789012",
                    "123456789012");
            var nu = new User("Nicolas",
                    "Molimini",
                    "nico@mymovies.com",
                    "nico",
                    "123456789012",
                    "123456789012");
            var lu = new User("Lucia",
                    "Molimini",
                    "lu@mymovies.com",
                    "lucia",
                    "123456789012",
                    "123456789012");
            var ju = new User("Josefina",
                    "Simini",
                    "jsimini@mymovies.com",
                    "jsimini",
                    "123456789012",
                    "123456789012");

            em.persist(nu);
            em.persist(lu);
            em.persist(ju);
            em.persist(eu);

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
