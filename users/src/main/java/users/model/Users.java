package users.model;

import common.date.DateTimeProvider;
import common.db.Tx;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import publisher.api.Publisher;
import publisher.api.data.users.NewUserEvent;
import users.api.AuthException;
import users.api.UserProfile;
import users.api.UsersException;
import users.api.UsersSubSystem;

public class Users implements UsersSubSystem {
    static final String USER_NAME_ALREADY_EXISTS = "userName already exists";
    static final String USER_ID_NOT_EXISTS = "User not registered";
    static final String USER_OR_PASSWORD_ERROR = "Invalid username or password";
    private static final int NUMBER_OF_RETRIES = 2;
    private final EntityManagerFactory emf;
    private final Token token;
    private final DateTimeProvider dateTimeProvider;
    private final Publisher publisher;

    public Users(EntityManagerFactory emf,
                 Token token, DateTimeProvider provider, Publisher publisher) {
        this.emf = emf;
        this.token = token;
        this.dateTimeProvider = provider;
        this.publisher = publisher;
    }

    public Users(EntityManagerFactory emf,
                 Token token, Publisher publisher) {
        this(emf, token, DateTimeProvider.create(), publisher);
    }

    @Override
    public String login(String username, String password) {
        return new Tx(emf).inTx(em -> {
            var q = em.createQuery(
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
        return new Tx(emf).inTxWithRetriesOnConflict((em) -> {
            checkUserNameAlreadyExists(userName, em);
            var user = new User(name, surname, email, userName,
                    password,
                    repeatPassword);
            em.persist(user);
            //within the Tx
            this.publisher.notify(em, new NewUserEvent(user.id(), user.userName(), user.email()));
            return user.id();
        }, NUMBER_OF_RETRIES);
    }

    private void checkUserNameAlreadyExists(String userName, EntityManager em) {
        var q = em.createQuery(
                "select u from User u where u.userName = ?1 ", User.class);
        q.setParameter(1, userName);
        var mightBeAUser = q.getResultList();
        if (!mightBeAUser.isEmpty()) {
            throw new UsersException(USER_NAME_ALREADY_EXISTS);
        }
    }

    @Override
    public Long userIdFrom(String token) {
        return this.token.verifyAndGetUserIdFrom(token);
    }

    @Override
    public UserProfile profileFrom(Long userId) {
        return new Tx(emf).inTx(em -> {
            return userBy(userId, em).toProfile();
        });
    }

    @Override
    public void changePassword(Long userId, String currentPassword,
                               String newPassword1, String newPassword2) {
        new Tx(emf).inTx(em -> {
            userBy(userId, em).changePassword(currentPassword, newPassword1,
                    newPassword2);
        });
    }

    User userBy(Long userId, EntityManager em) {
        return findByIdOrThrows(User.class, userId, Users.USER_ID_NOT_EXISTS, em);
    }

    <T> T findByIdOrThrows(Class<T> entity, Long id, String msg, EntityManager em) {
        var e = em.find(entity, id);
        if (e == null) {
            throw new UsersException(msg);
        }
        return e;
    }
}