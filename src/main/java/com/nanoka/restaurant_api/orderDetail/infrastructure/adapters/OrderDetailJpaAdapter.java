package com.nanoka.restaurant_api.orderDetail.infrastructure.adapters;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.nanoka.restaurant_api.orderDetail.aplication.ports.output.OrderDetailPersistencePort;
import com.nanoka.restaurant_api.orderDetail.domain.model.OrderDetail;
import com.nanoka.restaurant_api.orderDetail.infrastructure.repositories.OrderDetailRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderDetailJpaAdapter implements OrderDetailPersistencePort {

    private final OrderDetailRepository repository;

    @Override
    public Optional<OrderDetail> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public OrderDetail save(OrderDetail orderDetail) {
        return repository.save(orderDetail);
    }
}
