package com.nanoka.restaurant_api.productMovement.application.ports.output;

import com.nanoka.restaurant_api.productMovement.domain.model.ProductMovement;

import java.util.List;
import java.util.Optional;

public interface ProductMovementPersistencePort {
    Optional<ProductMovement> findById(Long id);
    Optional<ProductMovement> findByOrderIdAndProductId(Long orderId,Long productId);
    List<ProductMovement> findAllByProductId(Long productId);
    List<ProductMovement> findAllByOrderId(Long orderId);
    List<ProductMovement> findAll();
    ProductMovement save(ProductMovement productMovement);
    void delete(Long id);
    void deleteByOrderId(Long orderId);
}
