package com.nanoka.restaurant_api.category.application.ports.output;

import com.nanoka.restaurant_api.category.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryPersistencePort {
    Optional<Category> findById(Long id);
    Optional<Category> findByName(String name);
    List<Category> findAll();
    Category save(Category category);
    void deleteById(Long id);
}
