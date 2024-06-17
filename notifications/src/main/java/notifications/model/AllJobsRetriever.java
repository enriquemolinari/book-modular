package notifications.model;

import jakarta.persistence.EntityManager;

import java.util.List;

class AllJobsRetriever {
    private EntityManager em;

    public AllJobsRetriever(EntityManager em) {
        this.em = em;
    }

    List<NotificationJob> getAllJobs() {
        var query = em.createQuery("from NotificationJob nj order by nj.createdAt",
                NotificationJob.class);
        return query.getResultList();
    }
}
