package com.nanoka.restaurant_api.product.application.ports.output;

import com.nanoka.restaurant_api.product.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductPersistencePort {
    Optional<Product> findById(Long id);
    Optional<Product> findByName(String name);
    List<Product> findAll();
    Product save(Product product);
    void deleteById(Long id);
}
