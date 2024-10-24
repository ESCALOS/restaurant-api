package com.nanoka.restaurant_api.product.infrastructure.adapters.output.persistence.repository;

import com.nanoka.restaurant_api.product.infrastructure.adapters.output.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
