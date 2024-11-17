package com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest;

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

import com.nanoka.restaurant_api.product.application.ports.input.ProductServicePort;
import com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest.mapper.ProductRestMapper;
import com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest.model.request.DishCreateRequest;
import com.nanoka.restaurant_api.product.infrastructure.adapters.input.rest.model.response.DishResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dishes")
@PreAuthorize("isAuthenticated()")
public class DishController {
    private static final Logger logger = LoggerFactory.getLogger(DishController.class);
    private final ProductServicePort servicePort;
    private final ProductRestMapper restMapper;

    @GetMapping
    public List<DishResponse> findAll() {
        logger.info("Obteniendo todos los platos");
        return restMapper.toDishResponseList(servicePort.findAll(true));
    }

    @GetMapping("/{id}")
    public DishResponse findById(@PathVariable("id") Long id) {
        logger.info("Obteniendo plato con id: {}", id);
        return  restMapper.toDishResponse(servicePort.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DishResponse> save(@Valid @RequestBody DishCreateRequest request) {
        logger.info("Guardando nuevo plato");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toDishResponse(
                        servicePort.save(restMapper.toDish(request),true)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public DishResponse update(@PathVariable Long id, @Valid @RequestBody DishCreateRequest request) {
        logger.info("Actualizando plato con id: {}", id);
        return restMapper.toDishResponse(
                servicePort.update(id, restMapper.toDish(request)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        logger.info("Eliminando plato con id: {}", id);
        servicePort.delete(id);
    }
}
