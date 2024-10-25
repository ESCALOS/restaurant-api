package com.nanoka.restaurant_api.product.infrastructure.adapters;

import com.nanoka.restaurant_api.product.domain.model.Product;
import com.nanoka.restaurant_api.product.infrastructure.adapters.output.persistence.mapper.ProductPersistenceMapper;
import com.nanoka.restaurant_api.product.infrastructure.adapters.output.persistence.repository.ProductRepository;
import com.nanoka.restaurant_api.product.application.ports.output.ProductPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductPersistencePort {
    private final ProductRepository repository;
    private final ProductPersistenceMapper mapper;


    @Override
    public Optional<Product> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toProduct);
    }

    @Override
    public Optional<Product> findByName(String name) {
        return repository.findByName(name)
                .map(mapper::toProduct);
    }

    @Override
    public List<Product> findAll(Boolean isDish) {
        if(isDish) {
            return mapper.toProductList(repository.findByIsDishTrue());
        } else{
            return mapper.toProductList(repository.findByIsDishFalse());
        }
    }

    @Override
    public Product save(Product product) {
        return mapper.toProduct(repository.save(mapper.toProductEntity(product)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
