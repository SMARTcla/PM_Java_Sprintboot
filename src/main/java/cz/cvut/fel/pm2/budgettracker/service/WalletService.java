package cz.cvut.fel.pm2.budgettracker.service;

import cz.cvut.fel.pm2.budgettracker.model.*;
import cz.cvut.fel.pm2.budgettracker.repository.TransactionRepository;
import cz.cvut.fel.pm2.budgettracker.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Service class for managing wallet-related operations.
 */
@Service
@Transactional
public class WalletService {

    private TransactionService transactionService;

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;


    /**
     * Constructs a new WalletService with the given dependencies.
     *
     * @param walletRepository             The WalletDao to interact with wallet data.
     * @param transactionRepository        The TransactionDao to interact with transaction data.
     * @param transactionService    The TransactionService for transaction-related operations.
     */
    @Autowired
    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository, TransactionService transactionService) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;
    }

    /**
     * Creates a new wallet for the given user.
     *
     * @param name The name of the wallet.
     * @param user The user associated with the wallet.
     * @return The created wallet.
     */
    public Wallet createWallet(String name, User user) {
        Wallet newWallet = new Wallet();
        newWallet.setAmount(BigDecimal.valueOf(0));
        newWallet.setName(name + "Wallet");
        newWallet.setClient(user);
        newWallet.setBudgetLimit(BigDecimal.valueOf(100000));
        newWallet.setCurrency(Currency.CZK);
        walletRepository.save(newWallet);
        return newWallet;
    }

    /**
     * Updates the specified wallet.
     *
     * @param wallet The wallet to update.
     */
    public void updateWallet(Wallet wallet) {
        Objects.requireNonNull(wallet);
        walletRepository.save(wallet);
    }

    /**
     * Adds the specified amount of money to the wallet.
     *
     * @param wallet The wallet to add money to.
     * @param amount The amount of money to add.
     */
    public void addMoney(Wallet wallet, BigDecimal amount) {
        Objects.requireNonNull(wallet);
        wallet.setAmount(wallet.getAmount().add(amount));
        walletRepository.save(wallet);
    }

    /**
     * Retrieves the wallet associated with the specified user email.
     *
     * @param email The email of the user.
     * @return The wallet associated with the user email.
     */
    public Wallet getByClientEmail(String email) {
        return walletRepository.findByClientEmail(email);
    }

    /**
     * Retrieves the total balance of the specified wallet.
     *
     * @param walletId The ID of the wallet.
     * @return The total balance of the wallet.
     */
    public BigDecimal getTotalBalance(Long walletId) {
        Wallet wallet = getWalletById(walletId);
        if (wallet != null) {
            return wallet.getAmount();
        }
        return BigDecimal.ZERO;
    }

    /**
     * Retrieves the wallet with the specified ID.
     *
     * @param walletId The ID of the wallet.
     * @return The wallet with the specified ID.
     */
    public Wallet getWalletById(Long walletId) {
        return walletRepository.findById(walletId).orElseThrow(() -> new EntityNotFoundException("Wallet not found with id: "));
    }

    /**
     * Calculates the total income of the specified wallet.
     *
     * @param wallet The wallet to calculate the total income for.
     * @return The total income of the wallet.
     */
    public BigDecimal calculateTotalIncome(Wallet wallet) {
        BigDecimal totalIncome = BigDecimal.ZERO;

        for (Transaction transaction : wallet.getTransactions()) {
            if (transaction.getTypeTransaction() == TypeTransaction.INCOME) {
                totalIncome = totalIncome.add(transaction.getMoney());
            }
        }
        return totalIncome;
    }

    /**
     * Retrieves the transactions associated with the specified wallet ID.
     *
     * @param walletId The ID of the wallet.
     * @return The transactions associated with the wallet.
     */
    public List<Transaction> getTransactions(Long walletId) {
        Wallet wallet = getWalletById(walletId);
        return wallet.getTransactions();
    }

    /**
     * Calculates the budget progress of the specified wallet.
     *
     * @param walletId The ID of the wallet.
     * @return A map containing the total income, total expenses, and balance of the wallet.
     */
    public Map<String, BigDecimal> calculateBudgetProgress(Long walletId) {
        Wallet wallet = getWalletById(walletId);
        BigDecimal totalIncome = transactionService.calculateTotalIncome(wallet);
        BigDecimal totalExpenses = transactionService.calculateTotalExpenses(wallet);
        BigDecimal balance = totalIncome.subtract(totalExpenses);

        Map<String, BigDecimal> budgetProgress = new HashMap<>();
        budgetProgress.put("totalIncome", totalIncome);
        budgetProgress.put("totalExpenses", totalExpenses);
        budgetProgress.put("balance", balance);

        return budgetProgress;
    }


    /**
     * Adds a goal with the specified name and amount to the wallet.
     *
     * @param goal     The name of the goal.
     * @param money    The amount of money for the goal.
     * @param walletId The ID of the wallet.
     */
//    public void addGoal(String goal, BigDecimal money, Long walletId){
//        Wallet wallet = getWalletById(walletId);
//        Map<String, BigDecimal> currentBudgetGoals = wallet.getBudgetGoal();
//        if (!currentBudgetGoals.containsKey(goal)) {
//            currentBudgetGoals.put(goal, money);
//            wallet.setBudgetGoal(goal, money);
//        }
//    }

    /**
     * Changes the currency of the specified wallet.
     *
     * @param currency The new currency.
     * @param wallet   The wallet to update.
     */
    public void changeCurrency(Currency currency, Wallet wallet){
        switch (currency) {
            case CZK -> {
                if (wallet.getCurrency() == Currency.EUR) {
                    BigDecimal multipliedEURtoCZK = new BigDecimal("22");
                    wallet.setAmount(wallet.getAmount().multiply(multipliedEURtoCZK));
                    wallet.setBudgetLimit(wallet.getBudgetLimit().multiply(multipliedEURtoCZK));
                }
                if (wallet.getCurrency() == Currency.USD) {
                    BigDecimal multipliedUSDtoCZK = new BigDecimal("22.05");
                    wallet.setAmount(wallet.getAmount().multiply(multipliedUSDtoCZK));
                    wallet.setBudgetLimit(wallet.getBudgetLimit().multiply(multipliedUSDtoCZK));

                }
                wallet.setCurrency(Currency.CZK);
                walletRepository.save(wallet);
            }
            case EUR -> {
                if (wallet.getCurrency() == Currency.CZK) {
                    BigDecimal multipliedCZKtoEur = new BigDecimal("0.042");
                    wallet.setAmount(wallet.getAmount().multiply(multipliedCZKtoEur));
                    wallet.setBudgetLimit(wallet.getAmount().multiply(multipliedCZKtoEur));
                }
                if (wallet.getCurrency() == Currency.USD) {
                    BigDecimal multipliedUSDtoEUR = new BigDecimal("0.93");
                    wallet.setAmount(wallet.getAmount().multiply(multipliedUSDtoEUR));
                    wallet.setBudgetLimit(wallet.getBudgetLimit().multiply(multipliedUSDtoEUR));
                }
                wallet.setCurrency(Currency.EUR);
                walletRepository.save(wallet);
            }
            case USD -> {
                if (wallet.getCurrency() == Currency.EUR) {
                    BigDecimal multipliedEURtoUSD = new BigDecimal("1.07");
                    wallet.setAmount(wallet.getAmount().multiply(multipliedEURtoUSD));
                    wallet.setBudgetLimit(wallet.getBudgetLimit().multiply(multipliedEURtoUSD));
                }
                if (wallet.getCurrency() == Currency.CZK){
                    BigDecimal multipliedCZKToUSD = new BigDecimal("0.045");
                    wallet.setAmount(wallet.getAmount().multiply(multipliedCZKToUSD));
                    wallet.setBudgetLimit(wallet.getBudgetLimit().multiply(multipliedCZKToUSD));
                }
                wallet.setCurrency(Currency.USD);
                walletRepository.save(wallet);
            }
        }
    }


}
