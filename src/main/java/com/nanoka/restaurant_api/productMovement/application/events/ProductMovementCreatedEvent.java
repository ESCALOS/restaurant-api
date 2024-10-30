package com.nanoka.restaurant_api.productMovement.application.events;

import com.nanoka.restaurant_api.productMovement.domain.model.ProductMovement;

public record ProductMovementCreatedEvent(ProductMovement productMovement) {
}
