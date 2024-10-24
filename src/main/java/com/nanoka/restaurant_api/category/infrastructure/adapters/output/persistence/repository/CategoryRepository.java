package com.nanoka.restaurant_api.category.infrastructure.adapters.output.persistence.repository;

import com.nanoka.restaurant_api.category.infrastructure.adapters.output.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(String name);
}
