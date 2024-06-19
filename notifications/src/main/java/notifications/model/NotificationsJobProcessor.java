package notifications.model;

import common.db.Tx;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashSet;

public class NotificationsJobProcessor {
    private final JpaSession jpaSession;
    private final EntityManagerFactory emf;
    private final NotificationSender notificationSender;

    public NotificationsJobProcessor(EntityManagerFactory emf,
                                     NotificationSender notificationSender) {
        this.jpaSession = new JpaSession(emf);
        this.emf = emf;
        this.notificationSender = notificationSender;
    }

    public void processAll() {
        var allJobs = jpaSession.inSession((em) -> new AllJobsRetriever(em).getAllJobs());
        allJobs.forEach(
                (job) -> {
                    new Tx(emf).inTx(em -> {
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
}
