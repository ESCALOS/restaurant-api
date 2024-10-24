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
    public List<Product> findAll() { return persistencePort.findAll(); }

    @Override
    public Product save(Product product) {
        persistencePort.findByName(product.getName())
                .ifPresent(p -> {
                    throw new ConflictException(ErrorCatelog.PRODUCT_ALREADY_EXIST.getMessage());
                });

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
                    existingProduct.setIsDish(product.getIsDish());
                    existingProduct.setCategory(product.getCategory());
                    return persistencePort.save(existingProduct);
                })
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.PRODUCT_NOT_FOUND.getMessage()));
    }

    @Override
    public void delete(Long id) {
        persistencePort.findById(id)
                .orElseThrow(() ->new NotFoundException(ErrorCatelog.PRODUCT_NOT_FOUND.getMessage()));

        persistencePort.deleteById(id);
    }
}
