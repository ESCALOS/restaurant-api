package com.nanoka.restaurant_api.category.application.ports.input;

import com.nanoka.restaurant_api.category.domain.model.Category;

import java.util.List;

public interface CategoryServicePort {
    Category findById(Long id);
    List<Category> findAll();
    Category save(Category category);
    Category update(Long id,Category category);
    void delete(Long id);
}
