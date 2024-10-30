package com.nanoka.restaurant_api.product.application.services;

import com.nanoka.restaurant_api.product.application.ports.output.ProductPersistencePort;
import com.nanoka.restaurant_api.product.domain.model.Product;
import com.nanoka.restaurant_api.product.application.ports.input.ProductServicePort;
import com.nanoka.restaurant_api.util.ErrorCatelog;
import com.nanoka.restaurant_api.util.exceptions.ConflictException;
import com.nanoka.restaurant_api.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductServicePort {
    private final ProductPersistencePort persistencePort;

    @Override
    public Product findById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.PRODUCT_NOT_FOUND.getMessage()));
    }

    @Override
    public List<Product> findAll(Boolean isDish) { return persistencePort.findAll(isDish); }

    @Override
    public Product save(Product product, Boolean isDish) {
        persistencePort.findByName(product.getName())
                .ifPresent(p -> {
                    throw new ConflictException(ErrorCatelog.PRODUCT_ALREADY_EXIST.getMessage());
                });
        product.setIsDish(isDish);
        return persistencePort.save(product);
    }

    @Override
    public Product update(Long id,Product product) {
        return persistencePort.findById(id)
                .map(existingProduct -> {
                    persistencePort.findByName(product.getName())
                        .filter(p -> !p.getId().equals(id))
                                .ifPresent(p -> {
                                    throw new ConflictException(ErrorCatelog.PRODUCT_ALREADY_EXIST.getMessage());
                                });
                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setImageUrl(product.getImageUrl());
                    existingProduct.setCategory(product.getCategory());
                    existingProduct.setStock(product.getStock());
                    existingProduct.setMinStock(product.getMinStock());
                    return persistencePort.save(existingProduct);
                })
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.PRODUCT_NOT_FOUND.getMessage()));
    }

    @Override
    public void delete(Long id) {
        this.findById(id);

        persistencePort.deleteById(id);
    }

    @Override
    public Product modifyStock(Long id, Integer stock) {
        Product product = this.findById(id);
        product.setStock(stock + product.getStock());
        return persistencePort.save(product);
    }
}
