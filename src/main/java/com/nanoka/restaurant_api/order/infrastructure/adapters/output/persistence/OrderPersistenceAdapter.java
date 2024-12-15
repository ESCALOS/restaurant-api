package com.nanoka.restaurant_api.order.infrastructure.adapters.output.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.nanoka.restaurant_api.order.application.ports.output.OrderPersistencePort;
import com.nanoka.restaurant_api.order.domain.model.Order;
import com.nanoka.restaurant_api.order.infrastructure.adapters.output.persistence.mapper.OrderPersistenceMapper;
import com.nanoka.restaurant_api.order.infrastructure.adapters.output.persistence.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderPersistencePort {
    private final OrderRepository repository;
    private final OrderPersistenceMapper mapper;


    @Override
    public Optional<Order> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toOrder);
    }

    @Override
    public List<Order> findAll() {
        return mapper.toOrderList(repository.findAll());
    }

    @Override
    public Order save(Order order) {
        return mapper.toOrder(repository.save(mapper.toOrderEntity(order)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Order> findByPaidFalseAndTableId(Long tableId) {
        return repository.findByPaidFalseAndTableId(tableId)
                .map(mapper::toOrder);
    }
}
