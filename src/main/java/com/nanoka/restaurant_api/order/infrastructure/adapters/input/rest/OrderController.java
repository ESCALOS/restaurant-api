package com.nanoka.restaurant_api.order.infrastructure.adapters.input.rest;

import com.nanoka.restaurant_api.order.infrastructure.adapters.input.rest.mapper.OrderRestMapper;
import com.nanoka.restaurant_api.order.infrastructure.adapters.input.rest.model.request.OrderCreateRequest;
import com.nanoka.restaurant_api.order.infrastructure.adapters.input.rest.model.response.OrderResponse;
import com.nanoka.restaurant_api.order.application.ports.input.OrderServicePort;
import com.nanoka.restaurant_api.order.infrastructure.adapters.input.rest.model.response.OrderWithoutDetailResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@PreAuthorize("isAuthenticated()")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderServicePort servicePort;
    private final OrderRestMapper restMapper;

    @GetMapping
    public List<OrderWithoutDetailResponse> findAll() {
        logger.info("Obteniendo todas las órdenes");
        return restMapper.toOrderResponseListWithoutDetails(servicePort.findAll());
    }

    @GetMapping("/{id}")
    public OrderResponse findById(@PathVariable("id") Long id) {
        logger.info("Obteniendo orden con id: {}", id);
        return restMapper.toOrderResponse(servicePort.findById(id));
    }

    @PostMapping("/{tableId}")
    //@PreAuthorize("hasRole('WAITER')") y tambien los admin
    @PreAuthorize("hasRole('WAITER') or hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> save(@PathVariable(required = false) Long tableId, @Valid @RequestBody OrderCreateRequest request) {
        logger.info("Creando orden para tableId: {}", tableId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toOrderResponse(
                        servicePort.save(tableId, request)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('WAITER')")
    public void delete(@PathVariable Long id) {
        logger.info("Eliminando orden con id: {}", id);
        servicePort.delete(id);
    }

    @PostMapping("/{orderId}/products/{productId}")
    @PreAuthorize("hasRole('WAITER')")
    public ResponseEntity<OrderResponse> addProductToOrder(@PathVariable Long orderId, @PathVariable Long productId, @RequestParam int quantity) {
        logger.info("Añadiendo producto con id: {} a la orden con id: {}", productId, orderId);
        return ResponseEntity.ok(restMapper.toOrderResponse(servicePort.addProductToOrder(orderId, productId, quantity)));
    }

    @PutMapping("/{orderId}/products/{productId}")
    @PreAuthorize("hasRole('WAITER')")
    public ResponseEntity<OrderResponse> updateProductQuantity(@PathVariable Long orderId, @PathVariable Long productId, @RequestParam int quantity) {
        logger.info("Actualizando cantidad del producto con id: {} en la orden con id: {}", productId, orderId);
        return ResponseEntity.ok(restMapper.toOrderResponse(servicePort.updateProductQuantity(orderId, productId, quantity)));
    }

    @DeleteMapping("/{orderId}/products/{productId}")
    @PreAuthorize("hasRole('WAITER')")
    public ResponseEntity<Void> removeProductFromOrder(@PathVariable Long orderId, @PathVariable Long productId) {
        logger.info("Eliminando producto con id: {} de la orden con id: {}", productId, orderId);
        servicePort.removeProductFromOrder(orderId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/table/{tableId}")
    @PreAuthorize("hasRole('WAITER')")
    public OrderResponse getUnpaidOrderByTable(@PathVariable("tableId") Long tableId) {
        logger.info("Obteniendo orden con tableId: {}", tableId);
        return restMapper.toOrderResponse(servicePort.getUnpaidOrderByTable(tableId));
    }
}
