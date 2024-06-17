package notifications.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashSet;
import java.util.function.Consumer;

public class NotificationsJobProcessor {
    private final JpaSession jpaSession;
    private final EntityManagerFactory emf;
    private NotificationSender notificationSender;
    private EntityManager em;

    public NotificationsJobProcessor(EntityManagerFactory emf,
                                     NotificationSender notificationSender) {
        this.jpaSession = new JpaSession(emf);
        this.emf = emf;
        this.notificationSender = notificationSender;
    }

    public void processAll() {
        var allJobs = jpaSession.inSession((em) -> new AllJobsRetriever(em).getAllJobs());

        allJobs.stream().forEach(
                (job) -> {
                    inTx(em -> {
                        var info = job.asInfo();
                        var user = new UserRetriever(em).userRetriever(info.getIdUser());
                        notificationSender
                                .sendNewSaleEmailToTheUser(new HashSet<>(info.getPayedSeats()),
                                        info.getMovieName(),
                                        user.username(),
                                        info.getShowStartTime(),
                                        user.email(),
                                        info.getTotal());
                        var q = em.createQuery("delete from NotificationJob where id = ?1");
                        q.setParameter(1, job.id());
                        q.executeUpdate();
                    });
                }
        );
    }

    private void inTx(Consumer<EntityManager> toExecute) {
        em = emf.createEntityManager();
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
