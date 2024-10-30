package com.nanoka.restaurant_api.productMovement.infrastructure.adapters;

import com.nanoka.restaurant_api.productMovement.application.ports.output.ProductMovementPersistencePort;
import com.nanoka.restaurant_api.productMovement.domain.model.ProductMovement;
import com.nanoka.restaurant_api.productMovement.infrastructure.adapters.output.persistence.mapper.ProductMovementPersistentMapper;
import com.nanoka.restaurant_api.productMovement.infrastructure.adapters.output.persistence.repository.ProductMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductMovementPersistentAdapter implements ProductMovementPersistencePort {

    private final ProductMovementRepository repository;
    private final ProductMovementPersistentMapper mapper;


    @Override
    public Optional<ProductMovement> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toProductMovement);
    }

    @Override
    public List<ProductMovement> findAllByProductId(Long productId) {
        return mapper.toProductMovementList(repository.findAllByProductId(productId));
    }

    @Override
    public List<ProductMovement> findAllByOrderId(Long orderId) {
        return mapper.toProductMovementList(repository.findAllByOrderId(orderId));
    }

    @Override
    public Optional<ProductMovement> findByOrderIdAndProductId(Long orderId, Long productId) {
        return repository.findByOrderIdAndProductId(orderId,productId)
                .map(mapper::toProductMovement);
    }

    @Override
    public List<ProductMovement> findAll() {
        return mapper.toProductMovementList(repository.findAll());
    }

    @Override
    public ProductMovement save(ProductMovement productMovement) {
        return mapper.toProductMovement(repository.save(mapper.toProductMovementEntity(productMovement)));
    }

    @Override
    public void delete(Long id) { repository.deleteById(id); }

    @Override
    public void deleteByOrderId(Long orderId) {
        repository.deleteByOrderId(orderId);
    }
}
