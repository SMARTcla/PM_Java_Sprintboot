package cz.cvut.fel.pm2.budgettracker.repository;

import cz.cvut.fel.pm2.budgettracker.model.User;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDao extends BaseDao<User>{
    /**
     * Constructs a new BaseDao instance with the given type parameter.
     *
     * the class type of the entity managed by this BaseDao.
     */
    public UserDao() {
        super(User.class);
    }

    @Transactional
    public User findByEmail(String email){
        try {
            return em.createNamedQuery("User.findByEmail", User.class).setParameter("email", email )
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
