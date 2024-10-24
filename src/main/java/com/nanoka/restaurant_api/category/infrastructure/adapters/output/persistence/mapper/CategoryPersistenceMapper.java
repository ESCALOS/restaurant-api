package com.nanoka.restaurant_api.category.infrastructure.adapters.output.persistence.mapper;

import com.nanoka.restaurant_api.category.domain.model.Category;
import com.nanoka.restaurant_api.category.infrastructure.adapters.output.persistence.entity.CategoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryPersistenceMapper {
    CategoryEntity toCategoryEntity(Category category);
    Category toCategory(CategoryEntity entity);
    List<Category> toCategoryList(List<CategoryEntity> categoryList);
}
