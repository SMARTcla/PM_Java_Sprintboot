package cz.cvut.fel.pm2.budgettracker.repository;

import cz.cvut.fel.pm2.budgettracker.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByWallet_WalletId(Long walletId);

    @Query("SELECT g FROM Goal g WHERE g.wallet.walletId = :walletId")
    List<Goal> findAllByWallet_WalletId(@Param("walletId") Long walletId);
}
