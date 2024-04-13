package cz.cvut.fel.pm2.budgettracker.model;

/**
 * Represents a currency type.
 */
public enum Currency {
    EUR("EUR"),
    CZK("CZK"),
    USD("USD");

    Currency(String currency) {
        this.currency = currency;
    }

    private final String currency;

    @Override
    public String toString() {
        return currency;
    }
}
