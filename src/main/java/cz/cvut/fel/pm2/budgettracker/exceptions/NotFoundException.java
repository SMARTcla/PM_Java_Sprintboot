package cz.cvut.fel.pm2.budgettracker.exceptions;


public class NotFoundException extends RuntimeException{

    public NotFoundException(String message){
        super(message);
    }

}
