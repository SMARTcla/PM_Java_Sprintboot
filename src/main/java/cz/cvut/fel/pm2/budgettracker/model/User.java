package cz.cvut.fel.pm2.budgettracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a user entity in the system.
 */
@Entity
@Table(name = "client")
@NamedQueries({
        @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
        @NamedQuery(name = "User.deleteByEmail", query = "DELETE  FROM User u WHERE u.email = :email")
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 4L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "client_id")
    @Setter(AccessLevel.NONE)
    private Long clientId;

    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    private String email;

    @Basic(optional = false)
    @Column(nullable = false)
    private String password;

    @Basic(optional = false)
    @Column(nullable = false)
    private String username;

    @OneToOne(mappedBy = "client")
    private Wallet wallet;

    /**
     * Encodes the password using the provided password encoder.
     *
     * @param encoder The password encoder.
     */
    public void encodePassword(PasswordEncoder encoder) {
        this.password = encoder.encode(password);
    }
}
