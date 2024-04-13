package cz.cvut.fel.pm2.budgettracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a transaction entity in the system.
 */
@Entity
@Table(name = "transactions")
@NamedQueries({
        @NamedQuery(name = "findByName", query = "SELECT t FROM Transaction t where t.description = :name ")
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transactions_id")
    @Setter(AccessLevel.NONE)
    private Long transId;

    @Basic(optional = false)
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private String description;

    @Basic(optional = false)
    @Column(name = "trans_date", nullable = false, columnDefinition = "TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Setter(AccessLevel.NONE)
    private LocalDateTime date;

    @Basic(optional = false)
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private BigDecimal money;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    @Setter(AccessLevel.NONE)
    private TypeTransaction typeTransaction;

    @ManyToOne
    @JoinColumn(name = "wallet", referencedColumnName = "wallet_id")
    private Wallet wallet;

    @OneToOne
    @JoinColumn(name = "category", referencedColumnName = "name")
    @Setter(AccessLevel.NONE)
    private Category category;

    // Your custom setters remain as they were
    public void setDescription(String description) {
        if (!description.isEmpty()) {
            this.description = description;
        }
    }

    public void setDate(LocalDateTime date) {
        if (Objects.nonNull(date)) {
            this.date = date;
        }
    }

    public void setMoney(BigDecimal money) {
        if (Objects.nonNull(money)) {
            this.money = money;
        }
    }

    public void setTypeTransaction(TypeTransaction typeTransaction) {
        if (Objects.nonNull(typeTransaction)) {
            this.typeTransaction = typeTransaction;
        }
    }

    public void setCategory(Category category) {
        if (Objects.nonNull(category)) {
            this.category = category;
        }
    }
}