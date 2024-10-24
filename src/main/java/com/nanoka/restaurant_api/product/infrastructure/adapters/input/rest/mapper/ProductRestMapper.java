package com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest.mapper;

import com.nanoka.restaurant_api.category.application.ports.input.CategoryServicePort;
import com.nanoka.restaurant_api.category.domain.model.Category;
import com.nanoka.restaurant_api.product.domain.model.Product;
import com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest.model.request.ProductCreateRequest;
import com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest.model.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProductRestMapper {

    @Autowired
    private CategoryServicePort categoryServicePort;

    @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapCategoryIdToCategory")
    public abstract Product toProduct(ProductCreateRequest request);

    public abstract ProductResponse toProductResponse(Product product);

    public abstract List<ProductResponse> toProductResponseList(List<Product> productList);

    @Named("mapCategoryIdToCategory")
    public Category mapCategoryIdToCategory(Long categoryId) {
        return categoryServicePort.findById(categoryId);
    }
}
