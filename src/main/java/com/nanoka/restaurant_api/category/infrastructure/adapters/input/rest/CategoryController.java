package com.nanoka.restaurant_api.category.infrastructure.adapters.input.rest;

import com.nanoka.restaurant_api.category.application.ports.input.CategoryServicePort;
import com.nanoka.restaurant_api.category.infrastructure.adapters.input.rest.mapper.CategoryRestMapper;
import com.nanoka.restaurant_api.category.infrastructure.adapters.input.rest.model.request.CategoryCreateRequest;
import com.nanoka.restaurant_api.category.infrastructure.adapters.input.rest.model.response.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
@PreAuthorize("isAuthenticated()")
public class CategoryController {
    private final CategoryServicePort servicePort;
    private final CategoryRestMapper restMapper;

    @GetMapping
    public List<CategoryResponse> findAll() {
        return restMapper.toCategoryResponseList(servicePort.findAll());
    }

    @GetMapping("/{id}")
    public CategoryResponse findById(@PathVariable("id") Long id) {
        return  restMapper.toCategoryResponse(servicePort.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> save(@Valid @RequestBody CategoryCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toCategoryResponse(
                        servicePort.save(restMapper.toCategory(request))));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryCreateRequest request) {
        return restMapper.toCategoryResponse(
                servicePort.update(id, restMapper.toCategory(request)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        servicePort.delete(id);
    }
}
