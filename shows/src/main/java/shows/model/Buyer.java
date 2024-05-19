package shows.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static shows.model.Schema.DATABASE_SCHEMA_NAME;

@Entity
@Table(schema = DATABASE_SCHEMA_NAME)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PRIVATE)
@Getter(value = AccessLevel.PRIVATE)
public class Buyer {
    static final String INVALID_USERNAME = "A valid username must be provided";

    @Id
    private long id;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "purchaser")
    private List<Sale> purchases;

    public Buyer(long id) {
        this.id = id;
        this.purchases = new ArrayList<>();
    }

    void newPurchase(Sale sale) {
        this.purchases.add(sale);
    }

    Long id() {
        return id;
    }
}
