package cz.cvut.fel.pm2.budgettracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Represents a wallet entity in the system.
 */
@Entity
@Table(name = "wallet")
@NamedQueries({
        @NamedQuery(name = "findByClientEmail", query = "SELECT w FROM Wallet w WHERE w.client.email =:email")
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Wallet implements Serializable {

    @Serial
    private static final long serialVersionUID = 3L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "wallet_id")
    @Setter(AccessLevel.NONE)
    private Long walletId;

    @Basic(optional = false)
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private BigDecimal amount;

    @Enumerated(value = EnumType.STRING)
    @Setter(AccessLevel.NONE)
    private Currency currency;

    @Basic(optional = false)
    @Column(name = "budget_limit")
    @Setter(AccessLevel.NONE)
    private BigDecimal budgetLimit;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="client", referencedColumnName = "email")
    private User client;

    @Basic(optional = false)
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private String name;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    private List<Goal> goals = new ArrayList<>();


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "wallet")
    @Setter(AccessLevel.NONE)
    private List<Transaction> transactions;

    public void setAmount(BigDecimal amount) {
        if (Objects.nonNull(amount)){
            this.amount = amount;
        }
    }

    public void addGoal(Goal goal) {
        goals.add(goal);
        goal.setWallet(this);
    }

    public void removeGoal(Goal goal) {
        goals.remove(goal);
        goal.setWallet(null);
    }

    public void setCurrency(Currency currency) {
        if (Objects.nonNull(currency)){
            this.currency = currency;
        }
    }

    public void setBudgetLimit(BigDecimal budgetLimit) {
        if (Objects.nonNull(budgetLimit)){
            this.budgetLimit = budgetLimit;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (Objects.nonNull(name)){
            this.name = name;
        }
    }

    public void addTransaction(Transaction transaction){
        Objects.requireNonNull(transaction);
        if (Objects.isNull(transaction)) {
            transactions = new ArrayList<>();
        }
        transactions.add(transaction);
    }
}

