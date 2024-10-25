package com.nanoka.restaurant_api.product.application.ports.input;

import com.nanoka.restaurant_api.product.domain.model.Product;

import java.util.List;

public interface ProductServicePort {
    Product findById(Long id);
    List<Product> findAll(Boolean isDish);
    Product save(Product product, Boolean isDish);
    Product update(Long id,Product product);
    void delete(Long id);
}
