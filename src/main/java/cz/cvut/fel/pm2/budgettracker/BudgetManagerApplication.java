package cz.cvut.fel.pm2.budgettracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(
        exclude={SecurityAutoConfiguration.class}
)
public class BudgetManagerApplication {


    public static void main(String[] args) {
        SpringApplication.run(BudgetManagerApplication.class, args);

    }
}
