package com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest.mapper;

import com.nanoka.restaurant_api.product.domain.model.Product;
import com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest.model.request.ProductCreateRequest;
import com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest.model.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductRestMapper {
    Product toProduct(ProductCreateRequest request);
    ProductResponse toProductResponse(Product product);
    List<ProductResponse> toProductResponseList(List<Product> productList);
}
