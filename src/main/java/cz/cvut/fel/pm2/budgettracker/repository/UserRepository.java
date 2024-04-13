package cz.cvut.fel.pm2.budgettracker.repository;

import cz.cvut.fel.pm2.budgettracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
