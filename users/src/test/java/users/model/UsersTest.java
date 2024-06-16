package users.model;

import events.api.Publisher;
import events.api.data.users.NewUserEvent;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import users.api.AuthException;
import users.api.UsersException;

import static org.junit.jupiter.api.Assertions.*;
import static users.builder.PersistenceUnit.DERBY_EMBEDDED_USERS_MODULE;

public class UsersTest {

    private static final String JOSEUSER_SURNAME = "aSurname";
    private static final String JOSEUSER_NAME = "Jose";
    private static final String JOSEUSER_PASS = "password12345679";
    private static final String JOSEUSER_EMAIL = "jose@bla.com";
    private static final String JOSEUSER_USERNAME = "joseuser";
    private static final Long NON_EXISTENT_ID = -2L;
    private static EntityManagerFactory emf;
    private final ForTests tests = new ForTests();

    @BeforeEach
    public void setUp() {
        emf = Persistence.createEntityManagerFactory(DERBY_EMBEDDED_USERS_MODULE);
    }


    @Test
    public void loginOk() {
        var users = getUsers(tests.doNothingEventPubliser());
        registerUserJose(users);
        var token = users.login(JOSEUSER_USERNAME, JOSEUSER_PASS);
        assertEquals("aToken", token);
    }

    private Users getUsers(Publisher publisher) {
        return new Users(emf, tests.doNothingToken(), publisher);
    }

    @Test
    public void loginFail() {
        var users = getUsers(tests.doNothingEventPubliser());
        registerUserJose(users);
        var e = assertThrows(AuthException.class, () -> {
            users.login(JOSEUSER_USERNAME, "wrongPassword");
            fail("A user has logged in with a wrong password");
        });

        assertEquals(Users.USER_OR_PASSWORD_ERROR, e.getMessage());
    }

    @Test
    public void registerAUserNameTwice() {
        var users = getUsers(tests.doNothingEventPubliser());
        registerUserJose(users);

        var e = assertThrows(UsersException.class, () -> {
            registerUserJose(users);
            fail("I have registered the same userName twice");
        });

        assertEquals(Users.USER_NAME_ALREADY_EXISTS, e.getMessage());
    }

    @Test
    public void userChangePassword() {
        var users = getUsers(tests.doNothingEventPubliser());
        var userId = registerUserJose(users);
        users.changePassword(userId, JOSEUSER_PASS, "123412341234",
                "123412341234");
    }

    @Test
    public void userChangePasswordDoesNotMatch() {
        var cinema = getUsers(tests.doNothingEventPubliser());
        var userId = registerUserJose(cinema);
        var e = assertThrows(UsersException.class, () -> {
            cinema.changePassword(userId, JOSEUSER_PASS, "123412341234",
                    "123412341294");
        });
        assertEquals(User.PASSWORDS_MUST_BE_EQUALS, e.getMessage());
    }

    @Test
    public void userProfileFrom() {
        var users = getUsers(tests.doNothingEventPubliser());
        var userId = registerUserJose(users);
        var profile = users.profileFrom(userId);
        assertEquals(JOSEUSER_USERNAME, profile.username());
        assertEquals(JOSEUSER_EMAIL, profile.email());
        assertEquals(JOSEUSER_NAME + " " + JOSEUSER_SURNAME,
                profile.fullname());
    }

    @Test
    public void userIdNotExists() {
        var cinema = getUsers(tests.doNothingEventPubliser());
        var e = assertThrows(UsersException.class, () -> {
            cinema.profileFrom(NON_EXISTENT_ID);
            fail("UserId should not exists in the database");
        });
        assertEquals(Users.USER_ID_NOT_EXISTS, e.getMessage());
    }

    @Test
    public void addRegisterNewUserPublishEvent() {
        var publisher = new FakePublisher();
        var users = getUsers(publisher);
        var userId = users.registerUser(JOSEUSER_NAME, JOSEUSER_SURNAME,
                JOSEUSER_EMAIL,
                JOSEUSER_USERNAME,
                JOSEUSER_PASS, JOSEUSER_PASS);
        assertTrue(publisher.invokedWithEvent(new NewUserEvent(userId, JOSEUSER_USERNAME, JOSEUSER_EMAIL)));
    }

    private Long registerUserJose(Users users) {
        return users.registerUser(JOSEUSER_NAME, JOSEUSER_SURNAME,
                JOSEUSER_EMAIL,
                JOSEUSER_USERNAME,
                JOSEUSER_PASS, JOSEUSER_PASS);
    }

    @AfterEach
    public void tearDown() {
        emf.close();
    }

}
