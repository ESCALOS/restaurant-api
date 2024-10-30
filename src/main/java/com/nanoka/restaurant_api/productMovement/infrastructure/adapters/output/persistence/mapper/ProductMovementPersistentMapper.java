package com.nanoka.restaurant_api.productMovement.infrastructure.adapters.output.persistence.mapper;

import com.nanoka.restaurant_api.productMovement.domain.model.ProductMovement;
import com.nanoka.restaurant_api.productMovement.infrastructure.adapters.output.persistence.entity.ProductMovementEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMovementPersistentMapper {
    ProductMovementEntity toProductMovementEntity(ProductMovement productMovement);
    ProductMovement toProductMovement(ProductMovementEntity productMovementEntity);
    List<ProductMovement> toProductMovementList(List<ProductMovementEntity> productMovementEntities);
}
