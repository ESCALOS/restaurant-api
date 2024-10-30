package com.nanoka.restaurant_api.productMovement.infrastructure.adapters.output.persistence.repository;

import com.nanoka.restaurant_api.productMovement.infrastructure.adapters.output.persistence.entity.ProductMovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductMovementRepository extends JpaRepository<ProductMovementEntity, Long> {
    List<ProductMovementEntity> findAllByProductId(Long productId);
    List<ProductMovementEntity> findAllByOrderId(Long orderId);
    Optional<ProductMovementEntity> findByOrderIdAndProductId(Long productId, Long orderId);
    void deleteByOrderId(Long orderId);
}
