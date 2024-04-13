package cz.cvut.fel.pm2.budgettracker.repository;

import cz.cvut.fel.pm2.budgettracker.model.Category;
import cz.cvut.fel.pm2.budgettracker.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCategory(Category category);
    List<Transaction> findByMoney(BigDecimal money);
    List<Transaction> findByDescription(String description);
    List<Transaction> findByDate(LocalDateTime date);
    List<Transaction> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
