package com.nanoka.restaurant_api.productMovement.infrastructure.adapters.input.rest.mapper;

import com.nanoka.restaurant_api.product.application.ports.input.ProductServicePort;
import com.nanoka.restaurant_api.product.domain.model.Product;
import com.nanoka.restaurant_api.productMovement.domain.model.ProductMovement;
import com.nanoka.restaurant_api.productMovement.infrastructure.adapters.input.rest.model.request.ProductMovementItemRequest;
import com.nanoka.restaurant_api.productMovement.infrastructure.adapters.input.rest.model.response.ProductMovementResponse;
import com.nanoka.restaurant_api.productMovement.infrastructure.adapters.input.rest.model.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProductMovementRestMapper {
    @Autowired
    private ProductServicePort productServicePort;

    @Mapping(target = "product", source = "productId", qualifiedByName = "mapToProduct")
    public abstract ProductMovement toProductMovement(ProductMovementItemRequest request);

    @Mapping(target = "product", source = "product", qualifiedByName = "mapToProductResponse")
    public abstract ProductMovementResponse toProductMovementResponse(ProductMovement productMovement);

    public abstract List<ProductMovementResponse> toProductMovementResponseList(List<ProductMovement> productMovementList);

    @Named("mapToProduct")
    public Product mapToProduct(Long productId) {
        return productServicePort.findById(productId);
    }

    @Named("mapToProductResponse")
    public ProductResponse mapToProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getIsDish());
    }
}
