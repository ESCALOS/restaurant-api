package com.nanoka.restaurant_api.productMovement.infrastructure.listeners;

import com.nanoka.restaurant_api.product.application.ports.input.ProductServicePort;
import com.nanoka.restaurant_api.product.domain.model.Product;
import com.nanoka.restaurant_api.productMovement.application.events.ProductMovementCreatedEvent;
import com.nanoka.restaurant_api.productMovement.application.events.ProductMovementDeletedEvent;
import com.nanoka.restaurant_api.productMovement.application.events.ProductMovementUpdatedEvent;
import com.nanoka.restaurant_api.productMovement.domain.model.MovementTypeEnum;
import com.nanoka.restaurant_api.productMovement.domain.model.ProductMovement;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductStockUpdateListener {
    private final ProductServicePort productServicePort;

    @EventListener
    public void handleProductMovementCreated(ProductMovementCreatedEvent event) {
        ProductMovement movement = event.productMovement();
        Product product = productServicePort.findById(movement.getProduct().getId());

        int quantityAdjustment = (movement.getMovementType() == MovementTypeEnum.INPUT)
                ? movement.getQuantity()
                : -movement.getQuantity();

        productServicePort.modifyStock(product.getId(), quantityAdjustment);
    }

    @EventListener
    public void handleProductMovementUpdated(ProductMovementUpdatedEvent event) {
        ProductMovement oldMovement = event.oldMovement();
        ProductMovement newMovement = event.newMovement();
        Product product = productServicePort.findById(newMovement.getProduct().getId());

        int adjustment = 0;
        if (oldMovement.getMovementType() == MovementTypeEnum.INPUT) {
            adjustment -= oldMovement.getQuantity();
        } else {
            adjustment += oldMovement.getQuantity();
        }
        if (newMovement.getMovementType() == MovementTypeEnum.INPUT) {
            adjustment += newMovement.getQuantity();
        } else {
            adjustment -= newMovement.getQuantity();
        }
        productServicePort.modifyStock(product.getId(), adjustment);
    }

    @EventListener
    public void handleProductMovementDeleted(ProductMovementDeletedEvent event) {
        ProductMovement movement = event.productMovement();
        Product product = productServicePort.findById(movement.getProduct().getId());

        int quantityAdjustment = (movement.getMovementType() == MovementTypeEnum.INPUT)
                ? -movement.getQuantity()
                : movement.getQuantity();

        productServicePort.modifyStock(product.getId(), quantityAdjustment);
    }
}

