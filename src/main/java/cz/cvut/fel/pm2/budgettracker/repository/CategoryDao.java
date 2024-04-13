package cz.cvut.fel.pm2.budgettracker.repository;


import cz.cvut.fel.pm2.budgettracker.model.Category;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class CategoryDao extends BaseDao<Category> {
    /**
     * Constructs a new BaseDao instance with the given type parameter.
     *
     * the class type of the entity managed by this BaseDao.
     */

    public CategoryDao() {
        super(Category.class);
    }

    public Category getCategoryByName(String name) {
        TypedQuery<Category> query = em.createNamedQuery("findCategoryByName", Category.class);
        query.setParameter("name", name);
        List<Category> categories = query.getResultList();
        return categories.isEmpty() ? null : categories.get(0);
    }

    public Category updateCategoryByName(String name) {
        TypedQuery<Category> query = em.createNamedQuery("updateCategoryByName", Category.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }
}
