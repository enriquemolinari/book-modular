package shows.model;

import jakarta.persistence.*;
import lombok.*;
import shows.api.ShowsException;

import java.util.ArrayList;
import java.util.List;

import static shows.model.Schema.DATABASE_SCHEMA_NAME;

@Entity
@Table(schema = DATABASE_SCHEMA_NAME)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PRIVATE)
@Getter(value = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"userName"})
public class Buyer {
    static final String INVALID_USERNAME = "A valid username must be provided";
    static final String POINTS_MUST_BE_GREATER_THAN_ZERO = "Points must be greater than zero";

    @Id
    private long id;
    @Column(unique = true)
    private String userName;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "purchaser")
    private List<Sale> purchases;
    private String email;
    private int points;

    public Buyer(long id, String userName, String email) {
        this.userName = new NotBlankString(userName,
                INVALID_USERNAME).value();
        this.id = id;
        this.points = 0;
        this.email = email;
        this.purchases = new ArrayList<>();
    }

    void newEarnedPoints(int points) {
        if (points <= 0) {
            throw new ShowsException(POINTS_MUST_BE_GREATER_THAN_ZERO);
        }
        this.points += points;
    }

    public boolean hasPoints(int points) {
        return this.points == points;
    }

    public String userName() {
        return userName;
    }

    public boolean hasUsername(String aUserName) {
        return this.userName.equals(aUserName);
    }

    void newPurchase(Sale sale, int pointsWon) {
        this.newEarnedPoints(pointsWon);
        this.purchases.add(sale);
    }

    String email() {
        return this.email;
    }

    Long id() {
        return id;
    }
}
