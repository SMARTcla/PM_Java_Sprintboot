package cz.cvut.fel.pm2.budgettracker.repository;

import java.util.Collection;
import java.util.List;


/**
 * The BaseDao class is an abstract class that implements the GenericDao interface and
 * defines basic CRUD operations for entities in the database.
 * @param "<T>" the type of the entity managed by this BaseDao.
 */

public interface GenericDao<T> {

    /**
     Finds and returns the entity with the specified id.
     * @param id the id of the entity to find.
     * @return the entity with the specified id, or null if not found.
     */
    T find(Long id);


    /**
     * Finds and returns a list of all entities managed by this GenericDao.
     * @return a list of all entities managed by this GenericDao.
     */
    List<T> findAll();


    /**
     * Persists the specified entity to the database.
     * @param entity the entity to persist.
     * @throws NullPointerException if entity is null.
    */
    void persist(T entity);


    /**
     * Persists the specified collection of entities to the database.
     * @param entities the collection of entities to persist.
     * @throws NullPointerException if entities is null.
     */
    void persist(Collection<T> entities);


    /**
     * Updates the specified entity in the database.
     * @param entity the entity to update.
     * @return the updated entity.
     * @throws NullPointerException if entity is null.
     */
    T update(T entity);


    /**
     * Removes the specified entity from the database.
     *
     * @param entity the entity to be removed.
     * @throws NullPointerException if entity is null.
     */
    void remove(T entity);

    /**
     * Checks if an entity with the specified id exists in the database.
     * @param id the id of the entity to check.
     * @return true if an entity with the specified id exists, false otherwise.
     */
    boolean exists(Long id);

}
