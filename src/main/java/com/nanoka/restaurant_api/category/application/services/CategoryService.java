package com.nanoka.restaurant_api.category.application.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.nanoka.restaurant_api.category.application.ports.input.CategoryServicePort;
import com.nanoka.restaurant_api.category.application.ports.output.CategoryPersistencePort;
import com.nanoka.restaurant_api.category.domain.model.Category;
import com.nanoka.restaurant_api.util.ErrorCatelog;
import com.nanoka.restaurant_api.util.exceptions.ConflictException;
import com.nanoka.restaurant_api.util.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServicePort {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryPersistencePort persistencePort;

    @Override
    public Category findById(Long id) {
        logger.info("Finding category by id: {}", id);
        return persistencePort.findById(id)
                .orElseThrow(() -> {
                    logger.error("Category not found with id: {}", id);
                    return new NotFoundException(ErrorCatelog.CATEGORY_NOT_FOUND.getMessage());
                });
    }

    @Override
    public List<Category> findAll() {
        logger.info("Finding all categories");
        return persistencePort.findAll();
    }

    @Override
    public Category save(Category category) {
        logger.info("Saving category: {}", category.getName());
        persistencePort.findByName(category.getName())
                .ifPresent(c -> {
                    logger.error("Category already exists with name: {}", category.getName());
                    throw new ConflictException(ErrorCatelog.CATEGORY_ALREADY_EXIST.getMessage());
                });

        return persistencePort.save(category);
    }

    @Override
    public Category update(Long id, Category category) {
        logger.info("Updating category with id: {}", id);
        return persistencePort.findById(id)
                .map(existingCategory -> {
                    persistencePort.findByName(category.getName())
                            .filter(c -> !c.getId().equals(id))
                            .ifPresent(c -> {
                                logger.error("Category already exists with name: {}", category.getName());
                                throw new ConflictException(ErrorCatelog.CATEGORY_ALREADY_EXIST.getMessage());
                            });
                    existingCategory.setName(category.getName());
                    return persistencePort.save(existingCategory);
                })
                .orElseThrow(() -> {
                    logger.error("Category not found with id: {}", id);
                    return new NotFoundException(ErrorCatelog.CATEGORY_NOT_FOUND.getMessage());
                });
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting category with id: {}", id);
        persistencePort.findById(id)
                .orElseThrow(() -> {
                    logger.error("Category not found with id: {}", id);
                    return new NotFoundException(ErrorCatelog.CATEGORY_NOT_FOUND.getMessage());
                });

        try {
            // Intentar eliminar la categoría
            persistencePort.deleteById(id);
            logger.info("Category with id {} deleted successfully", id);
        } catch (DataIntegrityViolationException ex) {
            // Manejar la excepción de integridad referencial
            logger.error("Cannot delete category with id {}: {}", id, ex.getMessage());
            throw new ConflictException("No se puede eliminar la categoría porque tiene productos asociados.");
        }
    }
}
