package cz.cvut.fel.pm2.budgettracker.exceptions;

public class UserAlreadyExists extends RuntimeException{

    public UserAlreadyExists(String message) {
        super(message);
    }

    public static UserAlreadyExists create(String resourceName, Object identifier) {
        return new UserAlreadyExists(resourceName + " identified by " + identifier + " not found.");
    }
}
