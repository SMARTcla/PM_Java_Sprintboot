package cz.cvut.fel.pm2.budgettracker.repository;

import cz.cvut.fel.pm2.budgettracker.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByClientEmail(String email);
}
