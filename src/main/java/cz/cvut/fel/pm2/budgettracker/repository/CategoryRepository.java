package cz.cvut.fel.pm2.budgettracker.repository;

import cz.cvut.fel.pm2.budgettracker.model.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    @Modifying
    @Query("UPDATE Category c SET c.name = :newName WHERE c.name = :currentName")
    void updateCategoryByName(@Param("currentName") String currentName, @Param("newName") String newName);
}
