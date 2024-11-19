package com.nanoka.restaurant_api.category.infrastructure.adapters.input.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nanoka.restaurant_api.category.application.ports.input.CategoryServicePort;
import com.nanoka.restaurant_api.category.infrastructure.adapters.input.rest.mapper.CategoryRestMapper;
import com.nanoka.restaurant_api.category.infrastructure.adapters.input.rest.model.request.CategoryCreateRequest;
import com.nanoka.restaurant_api.category.infrastructure.adapters.input.rest.model.response.CategoryResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
@PreAuthorize("isAuthenticated()")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryServicePort servicePort;
    private final CategoryRestMapper restMapper;

    @GetMapping
    public List<CategoryResponse> findAll() {
        logger.info("Fetching all categories");
        return restMapper.toCategoryResponseList(servicePort.findAll());
    }

    @GetMapping("/{id}")
    public CategoryResponse findById(@PathVariable("id") Long id) {
        logger.info("Fetching category by id: {}", id);
        return  restMapper.toCategoryResponse(servicePort.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> save(@Valid @RequestBody CategoryCreateRequest request) {
        logger.info("Saving new category: {}", request.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toCategoryResponse(
                        servicePort.save(restMapper.toCategory(request))));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryCreateRequest request) {
        logger.info("Updating category with id: {}", id);
        return restMapper.toCategoryResponse(
                servicePort.update(id, restMapper.toCategory(request)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        logger.info("Deleting category with id: {}", id);
        servicePort.delete(id);
    }
}
