package cz.cvut.fel.pm2.budgettracker.model;

/**
 * Represents the type of transaction (income or expense).
 */
public enum TypeTransaction {
    INCOME("INCOME"),
    EXPENSE("EXPENSE");
    private String type;

    TypeTransaction(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
