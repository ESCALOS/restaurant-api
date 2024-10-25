package com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest;

import com.nanoka.restaurant_api.product.application.ports.input.ProductServicePort;
import com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest.mapper.ProductRestMapper;
import com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest.model.request.DishCreateRequest;
import com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest.model.response.DishResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dishes")
@PreAuthorize("isAuthenticated()")
public class DishController {
    private final ProductServicePort servicePort;
    private final ProductRestMapper restMapper;

    @GetMapping
    public List<DishResponse> findAll() {
        return restMapper.toDishResponseList(servicePort.findAll(true));
    }

    @GetMapping("/{id}")
    public DishResponse findById(@PathVariable("id") Long id) {
        return  restMapper.toDishResponse(servicePort.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DishResponse> save(@Valid @RequestBody DishCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toDishResponse(
                        servicePort.save(restMapper.toDish(request),true)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public DishResponse update(@PathVariable Long id, @Valid @RequestBody DishCreateRequest request) {
        return restMapper.toDishResponse(
                servicePort.update(id, restMapper.toDish(request)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        servicePort.delete(id);
    }
}
