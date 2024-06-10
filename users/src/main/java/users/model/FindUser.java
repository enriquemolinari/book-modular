package users.model;

import jakarta.persistence.EntityManager;
import users.api.UsersException;

public class FindUser {
    public User userBy(Long userId, EntityManager entityManager) {
        return findByIdOrThrows(entityManager, User.class, userId, Users.USER_ID_NOT_EXISTS);
    }

    <T> T findByIdOrThrows(EntityManager em, Class<T> entity, Long id, String msg) {
        var e = em.find(entity, id);
        if (e == null) {
            throw new UsersException(msg);
        }
        return e;
    }
}
