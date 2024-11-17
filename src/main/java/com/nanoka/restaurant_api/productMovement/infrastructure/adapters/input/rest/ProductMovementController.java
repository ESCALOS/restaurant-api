package com.nanoka.restaurant_api.productMovement.infrastructure.adapters.input.rest;

import com.nanoka.restaurant_api.productMovement.application.ports.input.ProductMovementServicePort;
import com.nanoka.restaurant_api.productMovement.infrastructure.adapters.input.rest.mapper.ProductMovementRestMapper;
import com.nanoka.restaurant_api.productMovement.infrastructure.adapters.input.rest.model.request.ProductMovementItemRequest;
import com.nanoka.restaurant_api.productMovement.infrastructure.adapters.input.rest.model.response.ProductMovementResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-movements")
@PreAuthorize("isAuthenticated()")
public class ProductMovementController {
    private final ProductMovementServicePort servicePort;
    private final ProductMovementRestMapper restMapper;

    private static final Logger logger = LoggerFactory.getLogger(ProductMovementController.class);

    @GetMapping
    public List<ProductMovementResponse> findAll(){
        logger.info("Obteniendo todos los movimientos de productos");
        return restMapper.toProductMovementResponseList(servicePort.findAll());
    }

    @GetMapping("/product/{productId}")
    public List<ProductMovementResponse> findByProductId(@PathVariable("productId") Long productId){
        logger.info("Obteniendo movimientos de productos para el producto con ID: {}", productId);
        return restMapper.toProductMovementResponseList(servicePort.findAllByProductId(productId));
    }

    @PostMapping
    public ResponseEntity<ProductMovementResponse> save(@Valid @RequestBody ProductMovementItemRequest productMovementRequest){
        logger.info("Guardando un nuevo movimiento de producto");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toProductMovementResponse(servicePort.save(restMapper.toProductMovement(productMovementRequest))));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductMovementResponse> updateQuantity(
            @PathVariable("id") Long id,
            @RequestParam int quantity) {
        logger.info("Actualizando la cantidad del movimiento de producto con ID: {}", id);
        return ResponseEntity.ok(restMapper.toProductMovementResponse(servicePort.update(id,quantity)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        logger.info("Eliminando el movimiento de producto con ID: {}", id);
        servicePort.delete(id);
    }

    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        logger.info("Exportando movimientos de productos a Excel");
        byte[] excelData = servicePort.exportProductsToExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "movimiento-de-productos.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }
}
