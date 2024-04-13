package cz.cvut.fel.pm2.budgettracker.repository;


import cz.cvut.fel.pm2.budgettracker.model.Category;
import cz.cvut.fel.pm2.budgettracker.model.Transaction;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TransactionDao extends BaseDao<Transaction>{
    /**
     * Constructs a new BaseDao instance with the given type parameter.
     *
     * the class type of the entity managed by this BaseDao.
     */
    public TransactionDao() {
        super(Transaction.class);
    }

    public List<Transaction> findByCategory(Category category){
        TypedQuery<Transaction> query = em.createQuery("SELECT t FROM Transaction t WHERE t.category = :category",
                Transaction.class);
        query.setParameter("category", category);
        return query.getResultList();
    }


    public List<Transaction> findByAmount(BigDecimal money) {
        TypedQuery<Transaction> query = em.createQuery("SELECT t FROM Transaction t WHERE t.money = :money",
                Transaction.class);
        query.setParameter("money", money);
        return query.getResultList();
    }

    public List<Transaction> findByDescription(String description) {
        TypedQuery<Transaction> query = em.createQuery("SELECT t FROM Transaction t WHERE t.description = :description", Transaction.class);
        query.setParameter("description", description);
        return query.getResultList();
    }

    public List<Transaction> findByDate(LocalDateTime date) {
        TypedQuery<Transaction> query = em.createQuery("SELECT t FROM Transaction t WHERE t.date = :date", Transaction.class);
        query.setParameter("date", date);
        return query.getResultList();
    }

    public List<Transaction> getTransactionsWithinInterval(LocalDateTime startDate, LocalDateTime endDate) {
        TypedQuery<Transaction> query = em.createQuery("SELECT t FROM Transaction t WHERE t.date >= :startDate AND t.date <= :endDate", Transaction.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }


}
