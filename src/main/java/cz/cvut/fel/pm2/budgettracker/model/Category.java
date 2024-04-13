package cz.cvut.fel.pm2.budgettracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a category entity in the system.
 */
@Entity
@Table(name = "category")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    @Setter(AccessLevel.NONE)
    private Long categoryId;

    @Basic(optional = false)
    @Column(nullable = false, name = "name", unique = true)
    private String name;
}


