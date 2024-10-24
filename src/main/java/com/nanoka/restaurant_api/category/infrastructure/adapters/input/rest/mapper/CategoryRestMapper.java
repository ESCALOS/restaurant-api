package com.nanoka.restaurant_api.category.infrastructure.adapters.input.rest.mapper;

import com.nanoka.restaurant_api.category.domain.model.Category;
import com.nanoka.restaurant_api.category.infrastructure.adapters.input.rest.model.request.CategoryCreateRequest;
import com.nanoka.restaurant_api.category.infrastructure.adapters.input.rest.model.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryRestMapper {
    Category toCategory(CategoryCreateRequest request);
    CategoryResponse toCategoryResponse(Category category);
    List<CategoryResponse> toCategoryResponseList(List<Category> categoryList);
}
