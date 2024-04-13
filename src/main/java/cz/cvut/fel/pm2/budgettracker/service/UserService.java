package cz.cvut.fel.pm2.budgettracker.service;

import cz.cvut.fel.pm2.budgettracker.model.User;
import cz.cvut.fel.pm2.budgettracker.model.Wallet;
import cz.cvut.fel.pm2.budgettracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Service class for managing user-related operations.
 */
@Service
@Transactional
public class UserService {
    private final UserRepository userDao;
    private final PasswordEncoder passwordEncoder;
    private final WalletService walletService;

    /**
     * Constructs a new UserService with the given dependencies.
     *
     * @param userDao         The UserDao to interact with the user data.
     * @param passwordEncoder The PasswordEncoder for encoding user passwords.
     * @param walletService   The WalletService for managing user wallets.
     */
    @Autowired
    public UserService(UserRepository userDao, PasswordEncoder passwordEncoder, WalletService walletService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.walletService = walletService;
    }

    /**
     * Creates a new user with the given email, username, and password.
     *
     * @param email    The email of the user.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return True if the user is created successfully, false otherwise.
     */
    public Boolean createUser(String email, String username, String password) {
        Objects.requireNonNull(email);
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        boolean result = false;
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.encodePassword(passwordEncoder);
        Wallet wallet = walletService.createWallet(username, user);
        user.setWallet(wallet);

        userDao.save(user);
        result = true;
        return result;
    }

    public User findUser(Long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

}
