package com.nanoka.restaurant_api.product.application.ports.input;

import com.nanoka.restaurant_api.product.domain.model.Product;

import java.util.List;

public interface ProductServicePort {
    Product findById(Long id);
    List<Product> findAll();
    Product save(Product product);
    Product update(Long id,Product product);
    void delete(Long id);
}
