package cz.cvut.fel.pm2.budgettracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Represents a goal entity in the system.
 */
@Entity
@Table(name = "goals")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Goal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "goals_id")
    @Setter(AccessLevel.NONE)
    private Long goalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column
    private String goal;

    @Column
    private BigDecimal moneyGoal;
}



