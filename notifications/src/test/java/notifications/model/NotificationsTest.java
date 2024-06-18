package notifications.model;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static notifications.builder.PersistenceUnit.DERBY_EMBEDDED_NOTIFICATIONS_MODULE;
import static notifications.model.NewSaleEmailTemplate.EMAIL_SUBJECT_SALE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NotificationsTest {
    private static EntityManagerFactory emf;
    private final String json1 = "{\"idUser\":1,\"pointsWon\":10,\"total\":100.0,\"payedSeats\":[1,2,3],\"movieName\":\"movie name\",\"showStartTime\":\"28/09 10:40\"}";

    @BeforeEach
    public void setUp() {
        emf = Persistence.createEntityManagerFactory(DERBY_EMBEDDED_NOTIFICATIONS_MODULE);
    }

    @AfterEach
    public void tearDown() {
        emf.close();
    }

    @Test
    public void notificationProcessorSendTheCorrectEmail() {
        insertJob(json1);
        insertJob(json1);
        createUser(1L, "username1", "username1@mail.com");
        var fakeEmailProvider = processAllJobs();
        String expectedBody = """
                Hello username1!
                You have new tickets!
                Here are the details of your booking:
                Movie: movie name
                Seats: 1,2,3
                Show time: 28/09 10:40
                Total paid: 100.0""";
        var jobs = new Notifications(emf).allJobs();
        assertTrue(jobs.isEmpty());
        var calls = new ArrayList<String[]>();
        calls.add(new String[]{"username1@mail.com",
                EMAIL_SUBJECT_SALE,
                expectedBody});
        calls.add(new String[]{"username1@mail.com",
                EMAIL_SUBJECT_SALE,
                expectedBody});
        assertEquals("username1@mail.com", fakeEmailProvider.invokedWith().get(0)[0]);
        assertEquals(EMAIL_SUBJECT_SALE, fakeEmailProvider.invokedWith().get(0)[1]);
        assertEquals(expectedBody, fakeEmailProvider.invokedWith().get(0)[2]);
        assertEquals("username1@mail.com", fakeEmailProvider.invokedWith().get(1)[0]);
        assertEquals(EMAIL_SUBJECT_SALE, fakeEmailProvider.invokedWith().get(1)[1]);
        assertEquals(expectedBody, fakeEmailProvider.invokedWith().get(1)[2]);
    }

    private FakeEmailProvider processAllJobs() {
        var fakeEmailProvider = new FakeEmailProvider();
        var jobProcessor = new NotificationsJobProcessor(emf,
                new NotificationSender(fakeEmailProvider));
        jobProcessor.processAll();
        return fakeEmailProvider;
    }

    @Test
    public void createdUserIsReturned() {
        createUser(2L, "username2", "username2@mail.com");
        var user = new Notifications(emf).userBy(2L);
        assertEquals("2", user[0]);
        assertEquals("username2", user[1]);
        assertEquals("username2@mail.com", user[2]);
    }

    @Test
    public void createdJobsAreReturned() {
        insertJob(json1);
        insertJob(json1);
        insertJob(json1);
        var jobs = new Notifications(emf).allJobs();
        assertEquals(3, jobs.size());
        assertEquals(json1, jobs.get(0)[1]);
        assertEquals(json1, jobs.get(1)[1]);
        assertEquals(json1, jobs.get(2)[1]);
    }

    private void insertJob(String emailNotificationInfo) {
        new JpaTx(emf).inTx((em) -> {
            Session session = em.unwrap(Session.class);
            session.doWork(new Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    new NotificationJobInsertStmt().insertJobStmt(connection, emailNotificationInfo);
                }
            });
        });
    }

    private void createUser(Long userId, String username, String email) {
        new JpaTx(emf).inTx(em -> em.persist(new User(userId, username, email)));
    }
}

class FakeEmailProvider implements EmailProvider {
    private final List<String[]> calls = new ArrayList<>();

    @Override
    public void send(String to, String subject, String body) {
        this.calls.add(new String[]{to, subject, body});
    }

    public List<String[]> invokedWith() {
        return this.calls;
    }
}