package cz.cvut.fel.pm2.budgettracker.service;

import cz.cvut.fel.pm2.budgettracker.exceptions.NotFoundException;
import cz.cvut.fel.pm2.budgettracker.model.Category;
import cz.cvut.fel.pm2.budgettracker.model.Transaction;
import cz.cvut.fel.pm2.budgettracker.model.User;
import cz.cvut.fel.pm2.budgettracker.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service class for managing categories.
 */
@Slf4j
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Constructs a new CategoryService with the provided CategoryDao.
     *
     * @param categoryDao The CategoryDao implementation used for data access.
     */
    @Autowired
    public CategoryService(CategoryRepository categoryDao) {
        this.categoryRepository = categoryDao;
    }


    /**
     * Creates a new category.
     *
     * @param category The category to create.
     */
    @Transactional
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    /**
     * Updates an existing category.
     *
     * @param category The updated category.
     * @return The updated category.
     */
    @Transactional
    @CachePut(value = "categories", key = "#category.getCategoryId()")
    public Category updateCategory(Category category) {
        categoryRepository.save(category);
        return category;
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id The ID of the category to delete.
     */
    @Transactional
    @CacheEvict(value = "categories", key = "#id")
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        if (Objects.isNull(category)) {
            throw new NotFoundException("Category with id " + id + " was not found");
        }
        categoryRepository.delete(category);
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id The ID of the category to retrieve.
     * @return The retrieved category.
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "categories", key = "#id")
    public Category getCategory(Long id) {
        Objects.requireNonNull(id);
        log.info("Fetching the category {} from DB", id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }

    /**
     * Retrieves all categories.
     *
     * @return The list of all categories.
     */
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

}