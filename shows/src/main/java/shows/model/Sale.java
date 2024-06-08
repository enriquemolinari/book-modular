package shows.model;

import common.date.FormattedDateTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shows.api.ShowsException;
import shows.api.Ticket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static shows.model.Schema.DATABASE_SCHEMA_NAME;

@Entity
@Table(schema = DATABASE_SCHEMA_NAME)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PRIVATE)
@Getter(value = AccessLevel.PRIVATE)
class Sale {

    public static final String SALE_CANNOT_BE_CREATED_WITHOUT_SEATS = "Sale cannot be created without seats";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private float total;
    private LocalDateTime salesDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private Buyer purchaser;

    private int pointsWon;

    @OneToMany
    @JoinColumn(name = "id_sale")
    private Set<ShowSeat> seatsSold;

    private Sale(float totalAmount,
                 Buyer buyerThatPurchased,
                 Set<ShowSeat> seatsSold,
                 int pointsWon) {
        this.total = totalAmount;
        this.purchaser = buyerThatPurchased;
        this.seatsSold = seatsSold;
        this.salesDate = LocalDateTime.now();
        this.pointsWon = pointsWon;
        buyerThatPurchased.newPurchase(this);
    }

    public static Ticket registerNewSaleFor(Buyer buyerThatPurchased,
                                            float totalAmount,
                                            int pointsWon,
                                            Set<ShowSeat> seatsSold) {
        checkSeatsNotEmpty(seatsSold);
        return new Sale(totalAmount, buyerThatPurchased, seatsSold,
                pointsWon).ticket();
    }

    private static void checkSeatsNotEmpty(Set<ShowSeat> seatsSold) {
        if (seatsSold.isEmpty()) {
            throw new ShowsException(SALE_CANNOT_BE_CREATED_WITHOUT_SEATS);
        }
    }

    private String formattedSalesDate() {
        return new FormattedDateTime(salesDate).toString();
    }

    List<Integer> confirmedSeatNumbers() {
        return this.seatsSold.stream().map(seat -> seat.seatNumber()).toList();
    }

    private Ticket ticket() {
        ShowSeat first = this.seatsSold.stream().findFirst().get();
        String movieName = first.showMovieName();
        String startTime = first.showStartTime();
        return new Ticket(total,
                pointsWon,
                formattedSalesDate(),
                confirmedSeatNumbers(),
                movieName,
                startTime);
    }
}
