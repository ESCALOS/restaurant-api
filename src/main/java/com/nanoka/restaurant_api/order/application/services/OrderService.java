package com.nanoka.restaurant_api.order.application.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nanoka.restaurant_api.order.application.ports.input.OrderServicePort;
import com.nanoka.restaurant_api.order.application.ports.output.OrderPersistencePort;
import com.nanoka.restaurant_api.order.domain.model.Order;
import com.nanoka.restaurant_api.order.infrastructure.adapters.input.rest.model.request.OrderCreateRequest;
import com.nanoka.restaurant_api.order.infrastructure.adapters.input.rest.model.request.OrderDetailCreateRequest;
import com.nanoka.restaurant_api.orderDetail.domain.model.OrderDetail;
import com.nanoka.restaurant_api.product.application.ports.input.ProductServicePort;
import com.nanoka.restaurant_api.product.domain.model.Product;
import com.nanoka.restaurant_api.productMovement.application.ports.input.ProductMovementServicePort;
import com.nanoka.restaurant_api.productMovement.domain.model.MovementTypeEnum;
import com.nanoka.restaurant_api.productMovement.domain.model.ProductMovement;
import com.nanoka.restaurant_api.table.application.ports.input.TableServicePort;
import com.nanoka.restaurant_api.table.domain.model.Table;
import com.nanoka.restaurant_api.user.application.ports.input.UserServicePort;
import com.nanoka.restaurant_api.user.domain.model.User;
import com.nanoka.restaurant_api.util.ErrorCatelog;
import com.nanoka.restaurant_api.util.exceptions.ConflictException;
import com.nanoka.restaurant_api.util.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderServicePort {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderPersistencePort persistencePort;
    private final UserServicePort userServicePort;
    private final TableServicePort tableServicePort;
    private final ProductServicePort productServicePort;
    private final ProductMovementServicePort productMovementServicePort;

    @Override
    public Order findById(Long id) {
        logger.info("Buscando orden con id: {}", id);
        return persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.ORDER_NOT_FOUND.getMessage()));
    }

    @Override
    public List<Order> findAll() {
        logger.info("Buscando todas las órdenes");
        return persistencePort.findAll();
    }

    @Override
    @Transactional
    public Order save(Long tableId, OrderCreateRequest request) {
        logger.info("Guardando orden para tableId: {}", tableId);
        // Obtén el usuario desde el contexto de seguridad
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userServicePort.findByUsername(username);

        // Crea la orden base
        Order order = new Order();
        order.setUser(user);
        order.setPaid(false);

        // Asigna la mesa si está presente
        if (tableId != null) {
            Table table = tableServicePort.findById(tableId);
            order.setTable(table);

            tableServicePort.toggleEnabled(tableId, false);
        }

        // Asigna temporalmente el total a cero
        order.setTotalAmount(BigDecimal.ZERO);

        // Primero, guarda la orden para obtener su ID
        order = persistencePort.save(order);

        // Lista para almacenar detalles de orden y movimientos de productos
        List<OrderDetail> orderDetails = new ArrayList<>();
        List<ProductMovement> movements = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // Manejo de productos en detalle
        for (OrderDetailCreateRequest detailRequest : request.getDetails()) {
            Product product = productServicePort.findById(detailRequest.getProductId());

            // Omitir el movimiento si es un plato
            if (product.getIsDish()) {
                continue;
            }

            // Verifica duplicados
            boolean productExists = orderDetails.stream()
                    .anyMatch(detail -> detail.getProduct().getId().equals(product.getId()));
            if (productExists) {
                throw new ConflictException(ErrorCatelog.DUPLICATE_PRODUCT_IN_ORDER.getMessage());
            }

            // Calcula el subtotal
            BigDecimal lineAmount = product.getPrice().multiply(BigDecimal.valueOf(detailRequest.getQuantity()));
            totalAmount = totalAmount.add(lineAmount);

            // Crea el detalle de la orden
            OrderDetail orderDetail = OrderDetail.builder()
                    .product(product)
                    .quantity(detailRequest.getQuantity())
                    .unitPrice(product.getPrice())
                    .quantityPaid(0)
                    .build();
            orderDetails.add(orderDetail);

            // Crea el movimiento de producto
            ProductMovement movement = new ProductMovement();
            movement.setOrder(order); // Ahora tiene el ID de la orden
            movement.setProduct(product);
            movement.setQuantity(detailRequest.getQuantity());
            movement.setMovementType(MovementTypeEnum.OUTPUT);
            movements.add(movement);
        }

        // Actualiza el total de la orden
        order.setTotalAmount(totalAmount);
        order.setDetailList(orderDetails);

        // Guarda la orden actualizada
        order = persistencePort.save(order);

        // Guarda todos los movimientos de producto
        movements.forEach(productMovementServicePort::save);

        return order;
    }

    /**
     * Update the order with the given ID.
     * @param id The ID of the order to update.
     * @param order The updated order.
     */
    public Order update(Long id, Order order) {
        logger.info("Actualizando orden con id: {}", id);
        return persistencePort.findById(id)
                .map(existingOrder -> {
                    order.setId(existingOrder.getId());
                    return persistencePort.save(order);
                })
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.ORDER_NOT_FOUND.getMessage()));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        logger.info("Eliminando orden con id: {}", id);
        // Obtén la orden
        Order order = persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.ORDER_NOT_FOUND.getMessage()));

        // Verifica el estado de la orden
        if (order.getPaid()) {
            throw new ConflictException("No se puede eliminar un pedido que ha sido pagado.");
        }

        if(order.getTable() != null) {
            tableServicePort.toggleEnabled(order.getTable().getId(),true);
        }

        try {
            // Elimina los movimientos de productos asociados y ajusta el stock
            List<ProductMovement> movements = productMovementServicePort.findAllByOrderId(id);
            for (ProductMovement movement : movements) {
                // Ajuste del stock: si es un OUTPUT, se debe devolver al stock
                int quantityAdjustment = movement.getMovementType() == MovementTypeEnum.OUTPUT
                        ? movement.getQuantity()
                        : -movement.getQuantity();
                productServicePort.modifyStock(movement.getProduct().getId(), quantityAdjustment);
            }

            System.out.println("Aca supuestamente elimina los movimientos");
            // Elimina todos los movimientos de producto asociados a esta orden
            productMovementServicePort.deleteByOrderId(id);

            // Finalmente, elimina la orden

            persistencePort.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("No se puede eliminar la orden porque tiene recibos asociados.");
        }
    }

    @Override
    @Transactional
    public Order addProductToOrder(Long orderId, Long productId, int quantity) {
        logger.info("Añadiendo producto con id: {} a la orden con id: {}", productId, orderId);
        Order order = findById(orderId);
        Product product = productServicePort.findById(productId);

        // Verifica si el producto es un plato
        boolean isDish = product.getIsDish();

        // Verifica si el producto ya está en la orden
        OrderDetail existingDetail = order.getDetailList().stream()
                .filter(detail -> detail.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existingDetail != null) {
            // Si ya existe, actualiza la cantidad
            existingDetail.setQuantity(existingDetail.getQuantity() + quantity);
        } else {
            // Crea un nuevo detalle de orden
            OrderDetail orderDetail = OrderDetail.builder()
                    .product(product)
                    .quantity(quantity)
                    .unitPrice(product.getPrice())
                    .build();
            order.getDetailList().add(orderDetail);
        }

        // Solo crea el movimiento de salida si el producto no es un plato
        if (!isDish) {
            // Crea el movimiento de salida
            ProductMovement movement = new ProductMovement();
            movement.setProduct(product);
            movement.setQuantity(quantity);
            movement.setMovementType(MovementTypeEnum.OUTPUT);
            movement.setOrder(order);

            // Registra el movimiento de producto
            productMovementServicePort.save(movement);
        }

        // Actualiza el total
        updateOrderTotal(order);

        return persistencePort.save(order);
    }

    private void updateOrderTotal(Order order) {
        BigDecimal totalAmount = order.getDetailList().stream()
                .map(detail -> detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);
    }

    @Override
    @Transactional
    public Order updateProductQuantity(Long orderId, Long productId, int quantity) {
        logger.info("Actualizando cantidad del producto con id: {} en la orden con id: {}", productId, orderId);
        Order order = findById(orderId);

        // Busca el detalle de la orden
        OrderDetail orderDetail = order.getDetailList().stream()
                .filter(detail -> detail.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.PRODUCT_NOT_FOUND_IN_ORDER.getMessage()));

        // Actualiza la cantidad del detalle de la orden
        orderDetail.setQuantity(quantity);

        // Si el producto no es un plato, actualiza el movimiento de salida
        if (!orderDetail.getProduct().getIsDish()) {
            ProductMovement movement = productMovementServicePort.findByOrderIdAndProductId(orderId, productId);
            productMovementServicePort.update(movement.getId(), quantity);
        }

        // Actualiza el total de la orden
        updateOrderTotal(order);

        // Guarda y retorna la orden actualizada
        return persistencePort.save(order);
    }

    @Override
    @Transactional
    public void removeProductFromOrder(Long orderId, Long productId) {
        logger.info("Eliminando producto con id: {} de la orden con id: {}", productId, orderId);
        Order order = findById(orderId);

        // Busca el detalle de la orden
        OrderDetail orderDetail = order.getDetailList().stream()
                .filter(detail -> detail.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.PRODUCT_NOT_FOUND_IN_ORDER.getMessage()));

        // Elimina el detalle de la orden
        order.getDetailList().remove(orderDetail);

        // Si el producto no es un plato, elimina el movimiento de salida correspondiente
        if (!orderDetail.getProduct().getIsDish()) {
            ProductMovement movement = productMovementServicePort.findByOrderIdAndProductId(orderId, productId);
            productMovementServicePort.delete(movement.getId());
        }

        // Actualiza el total de la orden
        updateOrderTotal(order);

        // Guarda la orden actualizada
        persistencePort.save(order);
    }
}
