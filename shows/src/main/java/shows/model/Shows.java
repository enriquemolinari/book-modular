package shows.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import shows.api.*;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class Shows implements ShowsSubSystem {
    static final int MINUTES_TO_KEEP_RESERVATION = 5;
    static final String MOVIE_ID_DOES_NOT_EXISTS = "Movie ID not found";
    static final String SHOW_TIME_ID_NOT_EXISTS = "Show ID not found";
    static final String BUYER_ID_NOT_EXISTS = "User not registered";
    static final String THEATER_ID_DOES_NOT_EXISTS = "Theater id not found";
    private final EntityManagerFactory emf;
    private final CreditCardPaymentProvider paymentGateway;
    private final DateTimeProvider dateTimeProvider;
    private EntityManager em;

    public Shows(EntityManagerFactory emf,
                 CreditCardPaymentProvider paymentGateway,
                 DateTimeProvider provider) {
        this.emf = emf;
        this.paymentGateway = paymentGateway;
        this.dateTimeProvider = provider;
    }

    public Shows(EntityManagerFactory emf,
                 CreditCardPaymentProvider paymentGateway) {
        this(emf, paymentGateway, DateTimeProvider.create());
    }

    @Override
    public List<MovieShows> showsUntil(LocalDateTime untilTo) {
        return inTx(em -> movieShowsUntil(untilTo));
    }

    private List<MovieShows> movieShowsUntil(LocalDateTime untilTo) {
        var query = em.createQuery(
                        "from Movie m "
                                + "join fetch m.showTimes s join fetch s.screenedIn "
                                + "where s.startTime >= ?1 and s.startTime <= ?2 "
                                + "order by m.name asc",
                        Movie.class).setParameter(1, LocalDateTime.now())
                .setParameter(2, untilTo);
        return query.getResultList().stream()
                .map(Movie::toMovieShow)
                .toList();
    }


    @Override
    public Long addNewTheater(String name, Set<Integer> seatsNumbers) {
        return inTx(em -> {
            var theater = new Theater(name, seatsNumbers);
            em.persist(theater);
            return theater.id();
        });
    }

    @Override
    public ShowInfo addNewShowFor(Long movieId, LocalDateTime startTime,
                                  float price, Long theaterId, int pointsToWin) {
        return inTx(em -> {
            var movie = movieBy(movieId);
            var theatre = theatreBy(theaterId);

            var showTime = new ShowTime(movie, startTime, price, theatre,
                    pointsToWin);
            //TODO: agregar showTime a Movie y correr tests...
            em.persist(showTime);
            return showTime.toShowInfo();
        });
    }

    @Override
    public DetailedShowInfo reserve(Long buyerId, Long showTimeId,
                                    Set<Integer> selectedSeats) {
        return inTx(em -> {
            ShowTime showTime = showTimeBy(showTimeId);
            var user = buyerBy(buyerId);
            showTime.reserveSeatsFor(user, selectedSeats,
                    this.dateTimeProvider.now().plusMinutes(MINUTES_TO_KEEP_RESERVATION));
            return showTime.toDetailedInfo();
        });
    }

    @Override
    public Ticket pay(Long userId, Long showTimeId, Set<Integer> selectedSeats,
                      String creditCardNumber, YearMonth expirationDate,
                      String secturityCode) {
        return inTx(em -> {
            ShowTime showTime = showTimeBy(showTimeId);
            var user = buyerBy(userId);
            return new Cashier(this.paymentGateway).paySeatsFor(selectedSeats,
                    showTime,
                    user,
                    Creditcard.of(creditCardNumber, expirationDate, secturityCode));
            //TODO: implement observer to notify Notifications module and Users for points
        });
    }

    private Theater theatreBy(Long theatreId) {
        return findByIdOrThrows(Theater.class, theatreId, THEATER_ID_DOES_NOT_EXISTS);
    }

    private Movie movieBy(Long movieId) {
        return findByIdOrThrows(Movie.class, movieId, MOVIE_ID_DOES_NOT_EXISTS);
    }

    private Buyer buyerBy(Long buyerId) {
        return findByIdOrThrows(Buyer.class, buyerId, BUYER_ID_NOT_EXISTS);
    }

    private ShowTime showTimeBy(Long id) {
        return findByIdOrThrows(ShowTime.class, id, SHOW_TIME_ID_NOT_EXISTS);
    }

    <T> T findByIdOrThrows(Class<T> entity, Long id, String msg) {
        var e = em.find(entity, id);
        if (e == null) {
            throw new ShowsException(msg);
        }
        return e;
    }

    @Override
    public DetailedShowInfo show(Long id) {
        return inTx(em -> {
            var show = showTimeBy(id);
            return show.toDetailedInfo();
        });
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
}