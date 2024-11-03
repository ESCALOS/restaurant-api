package com.nanoka.restaurant_api.productMovement.application.ports.input;

import com.nanoka.restaurant_api.productMovement.domain.model.ProductMovement;

import java.io.IOException;
import java.util.List;

public interface ProductMovementServicePort {
    ProductMovement findById(Long id);
    ProductMovement findByOrderIdAndProductId(Long orderId,Long productId);
    List<ProductMovement> findAll();
    List<ProductMovement> findAllByOrderId(Long orderId);
    List<ProductMovement> findAllByProductId(Long productId);
    ProductMovement save(ProductMovement productMovement);
    ProductMovement update(Long id, int newQuantity);
    void delete(Long id);
    void deleteByOrderId(Long orderId);
    byte[] exportProductsToExcel() throws IOException;
}
