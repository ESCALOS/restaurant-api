package com.nanoka.restaurant_api.product.infrastructure.adapters.output.persistence.repository;

import com.nanoka.restaurant_api.product.infrastructure.adapters.output.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByName(String name);
}
