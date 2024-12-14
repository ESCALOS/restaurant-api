package com.nanoka.restaurant_api.orderDetail.infrastructure.adapters;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.nanoka.restaurant_api.orderDetail.aplication.ports.output.OrderDetailPersistencePort;
import com.nanoka.restaurant_api.orderDetail.domain.model.OrderDetail;
import com.nanoka.restaurant_api.orderDetail.infrastructure.repositories.OrderDetailRepository;
import com.nanoka.restaurant_api.orderDetail.infrastructure.adapters.output.persistence.mapper.OrderDetailMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderDetailJpaAdapter implements OrderDetailPersistencePort {

    private final OrderDetailRepository repository;
    private final OrderDetailMapper mapper;

    @Override
    public Optional<OrderDetail> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toOrderDetail);
    }

    @Override
    public OrderDetail save(OrderDetail orderDetail) {
        return mapper.toOrderDetail(repository.save(mapper.toOrderDetailEntity(orderDetail)));
    }

    @Override
    public void updateQuantityPaid(Long id, int quantityPaid) {
        repository.updateQuantityPaid(id, quantityPaid);
    }
}
