package notifications.impl;

import jakarta.persistence.EntityManagerFactory;

import java.util.HashSet;

public class NotificationsJobProcessor {
    private final EntityManagerFactory emf;
    private final NotificationSender notificationSender;

    public NotificationsJobProcessor(EntityManagerFactory emf,
                                     NotificationSender notificationSender) {
        this.emf = emf;
        this.notificationSender = notificationSender;
    }

    public void processAll() {
        var allJobs = emf.callInTransaction((em) -> new AllJobsRetriever(em).getAllJobs());
        allJobs.forEach(
                (job) -> {
                    emf.runInTransaction(em -> {
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
