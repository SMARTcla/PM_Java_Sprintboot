package cz.cvut.fel.pm2.budgettracker.service;


import cz.cvut.fel.pm2.budgettracker.exceptions.NotFoundException;
import cz.cvut.fel.pm2.budgettracker.model.Category;
import cz.cvut.fel.pm2.budgettracker.model.Transaction;
import cz.cvut.fel.pm2.budgettracker.model.TypeTransaction;
import cz.cvut.fel.pm2.budgettracker.model.Wallet;
import cz.cvut.fel.pm2.budgettracker.repository.TransactionRepository;
import cz.cvut.fel.pm2.budgettracker.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository transactionDao;
    private final WalletRepository walletDao;

    /**
     * Constructs a new TransactionService with the provided TransactionDao and WalletDao.
     *
     * @param transactionDao The TransactionDao implementation used for data access.
     * @param walletDao      The WalletDao implementation used for data access.
     */
    @Autowired
    public TransactionService(TransactionRepository transactionDao, WalletRepository walletDao) {
        this.transactionDao = transactionDao;
        this.walletDao = walletDao;
    }

    /**
     * Retrieves all transactions.
     *
     * @return A list of all transactions.
     */
    @Transactional
    public List<Transaction> findAllTransactions(){
        return transactionDao.findAll();
    }

    /**
     * Retrieves a transaction by its ID.
     *
     * @param id The ID of the transaction.
     * @return The transaction with the specified ID, or null if not found.
     */
    @Transactional
    @Cacheable(value = "trans", key = "#id")
    public Transaction findTransactionById(Long id) {
        Objects.requireNonNull(id);
        log.info("Fetching the transaction {} from DB", id);

        return transactionDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: "));
    }

    /**
     * Retrieves transactions by category.
     *
     * @param category The category of the transactions.
     * @return A list of transactions with the specified category.
     */
    @Transactional
    public List<Transaction>  findTransactionByCategory(Category category){
        return transactionDao.findByCategory(category);
    }

    /**
     * Retrieves transactions by description.
     *
     * @param description The description of the transactions.
     * @return A list of transactions with the specified description.
     */
    @Transactional
    public List<Transaction> findTransactionsByMessage(String description){
        return transactionDao.findByDescription(description);
    }

    /**
     * Searches for transactions based on the provided criteria.
     *
     * @param category    The category of the transactions (optional).
     * @param date        The date of the transactions (optional).
     * @param description The description of the transactions (optional).
     * @param amount      The amount of the transactions (optional).
     * @return A list of transactions that match the specified criteria.
     */
    @Transactional(readOnly = true)
    public List<Transaction> searchTransactions(Category category, LocalDate date, String description, BigDecimal amount) {
        if (Objects.nonNull(category)) {
            return transactionDao.findByCategory(category);
        } else if (Objects.nonNull(date)) {
            LocalDateTime startDate = date.atStartOfDay();
            LocalDateTime endDate = date.atTime(LocalTime.MAX);
            return transactionDao.findAll();
        } else if (Objects.nonNull(description)) {
            return transactionDao.findByDescription(description);
        } else {
            return transactionDao.findAll();
        }
    }

    /**
     * Persists a new transaction.
     *
     * @param transaction The transaction to persist.
     */
    @Transactional
    public void persist(Transaction transaction) {
        Objects.requireNonNull(transaction);
        transactionDao.save(transaction);
    }

    /**
     * Updates a transaction.
     *
     * @param transaction The transaction to update.
     * @return The updated transaction.
     */
    @Transactional
    @CachePut(value = "trans", key = "#transaction.getTransId()")
    public Transaction update(Transaction transaction) {
        Objects.requireNonNull(transaction);
        log.info("Updated the transaction with id {}", transaction.getTransId());
        transactionDao.save(transaction);
        return transaction;
    }

    /**
     * Performs a transaction, updating the wallet's amount and type.
     *
     * @param transaction The transaction to perform.
     */
    @Transactional
    public void performTransaction(Transaction transaction) {
        Wallet wallet = transaction.getWallet();
        wallet.setAmount(wallet.getAmount().add(transaction.getMoney()));
        walletDao.save(wallet);

        if (transaction.getMoney().compareTo(BigDecimal.ZERO) >= 0) {
            transaction.setTypeTransaction(TypeTransaction.INCOME);
        } else {
            transaction.setTypeTransaction(TypeTransaction.EXPENSE);
        }
        transactionDao.save(transaction);
    }

    /**
     * Edits a transaction by updating its fields.
     *
     * @param transaction The updated transaction.
     */
    @Transactional
    public void editTransaction(Transaction transaction) {
        Transaction existingTransaction = transactionDao.findById(transaction.getTransId())
                        .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: "));
        if (Objects.isNull(existingTransaction)) {
            throw new NotFoundException("Transaction not found");
        }

        existingTransaction.setDescription(transaction.getDescription());
        existingTransaction.setCategory(transaction.getCategory());
        existingTransaction.setMoney(transaction.getMoney());
        existingTransaction.setTypeTransaction(transaction.getTypeTransaction());
        existingTransaction.setDate(transaction.getDate());
        // Update other relevant fields as needed!!!

        transactionDao.save(existingTransaction);
//        performTransaction(existingTransaction); //проверка на перерасчет после изменения транзакции
    }

    /**
     * Calculates the total expenses for a wallet based on its transactions.
     *
     * @param wallet The wallet.
     * @return The total expenses for the wallet.
     */
    @Transactional
    public BigDecimal calculateTotalExpenses(Wallet wallet) {
        List<Transaction> transactions = wallet.getTransactions();
        BigDecimal totalExpenses = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            if (transaction.getTypeTransaction() == TypeTransaction.EXPENSE) {
                totalExpenses = totalExpenses.add(transaction.getMoney());
            }
        }
        return totalExpenses;
    }

    /**
     * Calculates the total income for a wallet based on its transactions.
     *
     * @param wallet The wallet.
     * @return The total income for the wallet.
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalIncome(Wallet wallet) {
        BigDecimal totalIncome = BigDecimal.ZERO;

        // Iterate through the transactions in the wallet and calculate the total income
        for (Transaction transaction : wallet.getTransactions()) {
            if (transaction.getTypeTransaction() == TypeTransaction.INCOME) {
                totalIncome = totalIncome.add(transaction.getMoney());
            }
        }

        return totalIncome;
    }

    /**
     * Deletes a transaction by its ID.
     *
     * @param id The ID of the transaction to delete.
     */
    @Transactional
    @CacheEvict(value = "trans", key = "#id")
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + id));
        if (Objects.isNull(transaction)) {
            throw new NotFoundException("Transaction not found");
        }

        transactionDao.delete(transaction);
    }

    /**
     * Exports transactions to a text file.
     *
     * @return A Resource representing the exported text file.
     * @throws IOException If an I/O error occurs while exporting the transactions.
     */
    public Resource exportTransactionsToTxtFile() throws IOException {
        String filePath = "server/src/main/resources/transactions.txt";
        List<Transaction> transactions = transactionDao.findAll();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Transaction transaction : transactions) {
                writer.write("Date: " + formatTextField(transaction.getDate().toString()));
                writer.newLine();
                writer.write("Description: " + formatTextField(transaction.getDescription()));
                writer.newLine();
                writer.write("Category: " + formatTextField(transaction.getCategory().getName()));
                writer.newLine();
                writer.write("Amount: " + formatTextField(transaction.getMoney().toString()));
                writer.newLine();
                writer.newLine();
            }
        }
        return new FileSystemResource(filePath);
    }

    /**
     * Formats a text field for the exported transactions file.
     *
     * @param field The text field to format.
     * @return The formatted text field.
     */
    private String formatTextField(String field) {
        if (Objects.isNull(field)) {
            return "";
        }
        return field;
    }

}
