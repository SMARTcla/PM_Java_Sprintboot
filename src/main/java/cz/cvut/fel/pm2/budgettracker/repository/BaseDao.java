package cz.cvut.fel.pm2.budgettracker.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


/**
 * The BaseDao class is an abstract class that implements the GenericDao interface and
 * defines basic CRUD operations for entities in the database.
 * @param <T> the type of the entity managed by this BaseDao.
 */
public abstract class BaseDao<T> implements GenericDao<T>{

    // The EntityManager used to interact with the persistence context.
    @PersistenceContext
    protected EntityManager em;

    // The class type of the entity managed by this BaseDao
    protected final Class<T> type;

    /**
     *
     * Constructs a new BaseDao instance with the given type parameter.
     * @param type the class type of the entity managed by this BaseDao.
     */
    public BaseDao(Class<T> type){
        this.type = type;
    }

    @Override
    public T find(Long id) {
        Objects.requireNonNull(id);
        return em.find(type, id);
    }

    @Override
    public List<T> findAll() {
        try {
            return em.createQuery("SELECT e FROM " + type.getSimpleName() + " e", type).getResultList();
        } catch (RuntimeException ex) {
            throw new PersistenceException(ex);
        }
    }

    @Override
    public void persist(T entity) {
        Objects.requireNonNull(entity);
        try {
            em.persist(entity);
        } catch (RuntimeException ex) {
            throw new PersistenceException(ex);
        }
    }

    @Override
    public void persist(Collection<T> entities) {
        Objects.requireNonNull(entities);
        if (entities.isEmpty()) return;
        try {
            entities.forEach(this::persist);
        } catch (RuntimeException ex) {
            throw new PersistenceException(ex);
        }
    }

    @Override
    public T update(T entity) {
        Objects.requireNonNull(entity);
        try {
            return em.merge(entity);
        } catch (RuntimeException ex) {
            throw new PersistenceException(ex);
        }
    }

    @Override
    public void remove(T entity) {
        Objects.requireNonNull(entity);
        try {
            final T toRemove = em.merge(entity);
            if (toRemove != null) em.remove(entity);
        } catch (RuntimeException ex) {
            throw new PersistenceException(ex);
        }
    }

    @Override
    public boolean exists(Long id) {
        return id != null && em.find(type, id) != null;
    }

}
