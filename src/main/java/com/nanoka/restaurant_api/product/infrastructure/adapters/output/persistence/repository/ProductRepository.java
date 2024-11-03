package com.nanoka.restaurant_api.product.infrastructure.adapters.output.persistence.repository;

import com.nanoka.restaurant_api.product.infrastructure.adapters.output.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByName(String name);
    List<ProductEntity> findByIsDishTrue();
    List<ProductEntity> findByIsDishFalse();
    // Método para obtener productos con stock menor al mínimo
    List<ProductEntity> findByStockLessThanMinStock();
}
