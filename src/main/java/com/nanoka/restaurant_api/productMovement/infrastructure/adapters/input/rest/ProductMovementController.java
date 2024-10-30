package com.nanoka.restaurant_api.productMovement.infrastructure.adapters.input.rest;

import com.nanoka.restaurant_api.productMovement.application.ports.input.ProductMovementServicePort;
import com.nanoka.restaurant_api.productMovement.infrastructure.adapters.input.rest.mapper.ProductMovementRestMapper;
import com.nanoka.restaurant_api.productMovement.infrastructure.adapters.input.rest.model.request.ProductMovementItemRequest;
import com.nanoka.restaurant_api.productMovement.infrastructure.adapters.input.rest.model.response.ProductMovementResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-movements")
@PreAuthorize("isAuthenticated()")
public class ProductMovementController {
    private final ProductMovementServicePort servicePort;
    private final ProductMovementRestMapper restMapper;

    @GetMapping
    public List<ProductMovementResponse> findAll(){
        return restMapper.toProductMovementResponseList(servicePort.findAll());
    }

    @GetMapping("/product/{productId}")
    public List<ProductMovementResponse> findByProductId(@PathVariable("productId") Long productId){
        return restMapper.toProductMovementResponseList(servicePort.findAllByProductId(productId));
    }

    @PostMapping
    public ResponseEntity<ProductMovementResponse> save(@Valid @RequestBody ProductMovementItemRequest productMovementRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toProductMovementResponse(servicePort.save(restMapper.toProductMovement(productMovementRequest))));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductMovementResponse> updateQuantity(
            @PathVariable("id") Long id,
            @RequestParam int quantity) {
        return ResponseEntity.ok(restMapper.toProductMovementResponse(servicePort.update(id,quantity)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        servicePort.delete(id);
    }
}
