package notifications.impl;

import jakarta.persistence.EntityManager;

class UserRetriever {
    private final EntityManager em;

    public UserRetriever(EntityManager em) {
        this.em = em;
    }

    User userRetriever(Long id) {
        User user = em.find(User.class, id);
        if (user == null) {
            throw new RuntimeException(Notifications.USER_DOES_NOT_EXISTS);
        }
        return user;
    }
}
