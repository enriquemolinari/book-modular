package users.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.RollbackException;
import users.api.AuthException;
import users.api.UserProfile;
import users.api.UsersException;
import users.api.UsersSubSystem;

import java.util.function.Function;

public class Users implements UsersSubSystem {
    public static final String USER_OR_PASSWORD_ERROR = "Invalid username or password";
    static final String USER_NAME_ALREADY_EXISTS = "userName already exists";
    static final String USER_ID_NOT_EXISTS = "User not registered";
    private static final int NUMBER_OF_RETRIES = 2;
    private final EntityManagerFactory emf;
    private final Token token;
    private final DateTimeProvider dateTimeProvider;
    private EntityManager em;

    public Users(EntityManagerFactory emf,
                 Token token, DateTimeProvider provider) {
        this.emf = emf;
        this.token = token;
        this.dateTimeProvider = provider;
    }

    public Users(EntityManagerFactory emf,
                 Token token) {
        this(emf, token, DateTimeProvider.create());
    }

    @Override
    public String login(String username, String password) {
        return inTx(em -> {
            var q = this.em.createQuery(
                    "select u from User u where u.userName = ?1 and u.password.password = ?2",
                    User.class);
            q.setParameter(1, username);
            q.setParameter(2, password);
            var mightBeAUser = q.getResultList();
            if (mightBeAUser.isEmpty()) {
                throw new AuthException(USER_OR_PASSWORD_ERROR);
            }
            var user = mightBeAUser.get(0);
            em.persist(new LoginAudit(this.dateTimeProvider.now(), user));
            return token.tokenFrom(user.toMap());
        });
    }

    @Override
    public Long registerUser(String name, String surname, String email,
                             String userName,
                             String password, String repeatPassword) {
        return inTxWithRetriesOnConflict((em) -> {
            checkUserNameAlreadyExists(userName);
            var user = new User(name, surname, email, userName,
                    password,
                    repeatPassword);
            em.persist(user);
            return user.id();
        });
    }

    private void checkUserNameAlreadyExists(String userName) {
        var q = this.em.createQuery(
                "select u from User u where u.userName = ?1 ", User.class);
        q.setParameter(1, userName);
        var mightBeAUser = q.getResultList();
        if (!mightBeAUser.isEmpty()) {
            throw new UsersException(USER_NAME_ALREADY_EXISTS);
        }
    }

    private User userBy(Long userId) {
        return findByIdOrThrows(User.class, userId, USER_ID_NOT_EXISTS);
    }

    <T> T findByIdOrThrows(Class<T> entity, Long id, String msg) {
        var e = em.find(entity, id);
        if (e == null) {
            throw new UsersException(msg);
        }
        return e;
    }

    private <T> T inTx(Function<EntityManager, T> toExecute) {
        em = emf.createEntityManager();
        var tx = em.getTransaction();

        try {
            tx.begin();

            T t = toExecute.apply(em);
            tx.commit();

            return t;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private <T> T inTxWithRetriesOnConflict(
            Function<EntityManager, T> toExecute) {
        int retries = 0;

        while (retries < Users.NUMBER_OF_RETRIES) {
            try {
                return inTx(toExecute);
                // There is no a great way in JPA to detect a constraint
                // violation. I use RollbackException and retries one more
                // time for specific use cases
            } catch (RollbackException e) {
                // jakarta.persistence.RollbackException
                retries++;
            }
        }
        throw new UsersException(
                "Trasaction could not be completed due to concurrency conflic");
    }

    @Override
    public Long userIdFrom(String token) {
        return this.token.verifyAndGetUserIdFrom(token);
    }

    @Override
    public UserProfile profileFrom(Long userId) {
        return inTx(em -> userBy(userId).toProfile());
    }

    @Override
    public void changePassword(Long userId, String currentPassword,
                               String newPassword1, String newPassword2) {
        inTx(em -> {
            userBy(userId).changePassword(currentPassword, newPassword1,
                    newPassword2);
            // just to conform the compiler
            return null;
        });
    }
}