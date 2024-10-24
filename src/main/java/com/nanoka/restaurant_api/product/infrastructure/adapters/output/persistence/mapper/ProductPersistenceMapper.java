package com.nanoka.restaurant_api.product.infrastructure.adapters.output.persistence.mapper;

import com.nanoka.restaurant_api.product.domain.model.Product;
import com.nanoka.restaurant_api.product.infrastructure.adapters.output.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductPersistenceMapper {
    ProductEntity toProductEntity(Product product);
    Product toProduct(ProductEntity productEntity);
    List<Product> toProductList(List<ProductEntity> productList);
}
