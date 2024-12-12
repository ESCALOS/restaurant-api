package com.nanoka.restaurant_api.orderDetail.aplication.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nanoka.restaurant_api.category.application.services.CategoryService;
import com.nanoka.restaurant_api.orderDetail.aplication.ports.input.OrderDetailServicePort;
import com.nanoka.restaurant_api.orderDetail.aplication.ports.output.OrderDetailPersistencePort;
import com.nanoka.restaurant_api.orderDetail.domain.model.OrderDetail;
import com.nanoka.restaurant_api.util.ErrorCatelog;
import com.nanoka.restaurant_api.util.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements OrderDetailServicePort {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    private final OrderDetailPersistencePort persistencePort;

    @Override
    public OrderDetail findById(Long id) {
        logger.info("Finding orderDetail by id: {}", id);
        return persistencePort.findById(id).orElseThrow(() -> {
            logger.error("OrderDetail not found with id: {}", id);
            return new NotFoundException(ErrorCatelog.ORDER_DETAIL_NOT_FOUND.getMessage());
        });
    }

    @Override
    @Transactional
    public OrderDetail save(OrderDetail orderDetail) {
        logger.info("Saving orderDetail: {}", orderDetail);
        return persistencePort.save(orderDetail);
    }

    @Override
    public OrderDetail update(Long id, OrderDetail orderDetail) {
        logger.info("Updating orderDetail with id: {}", id);
        return persistencePort.findById(id)
                .map(existingOrderDetail -> {
                    orderDetail.setId(existingOrderDetail.getId());
                    return persistencePort.save(orderDetail);
                })
                .orElseThrow(() -> {
                    logger.error("OrderDetail not found with id: {}", id);
                    return new NotFoundException(ErrorCatelog.ORDER_DETAIL_NOT_FOUND.getMessage());
                });
    }

    @Override
    public void updateQuantityPaid(Long id, int quantityPaid) {
        logger.info("Updating quantityPaid of orderDetail with id: {}", id);
        persistencePort.findById(id)
                .map(orderDetail -> {
                    orderDetail.setQuantityPaid(quantityPaid);
                    return persistencePort.save(orderDetail);
                })
                .orElseThrow(() -> {
                    logger.error("OrderDetail not found with id: {}", id);
                    return new NotFoundException(ErrorCatelog.ORDER_DETAIL_NOT_FOUND.getMessage());
                });
    }
}
