package com.nanoka.restaurant_api.productMovement.application.services;

import com.nanoka.restaurant_api.productMovement.application.events.ProductMovementCreatedEvent;
import com.nanoka.restaurant_api.productMovement.application.events.ProductMovementDeletedEvent;
import com.nanoka.restaurant_api.productMovement.application.events.ProductMovementUpdatedEvent;
import com.nanoka.restaurant_api.productMovement.application.ports.input.ProductMovementServicePort;
import com.nanoka.restaurant_api.productMovement.application.ports.output.ProductMovementPersistencePort;
import com.nanoka.restaurant_api.productMovement.domain.model.ProductMovement;
import com.nanoka.restaurant_api.user.application.ports.input.UserServicePort;
import com.nanoka.restaurant_api.user.domain.model.User;
import com.nanoka.restaurant_api.util.ErrorCatelog;
import com.nanoka.restaurant_api.util.exceptions.ConflictException;
import com.nanoka.restaurant_api.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductMovementService implements ProductMovementServicePort {

    private final ProductMovementPersistencePort persistencePort;
    private final ApplicationEventPublisher eventPublisher;
    private final UserServicePort userServicePort;

    @Override
    public ProductMovement findById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.PRODUCT_MOVEMENT_NOT_FOUND.getMessage()));
    }

    @Override
    public ProductMovement findByOrderIdAndProductId(Long orderId, Long productId) {
        return persistencePort.findByOrderIdAndProductId(orderId,productId)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.PRODUCT_MOVEMENT_NOT_FOUND.getMessage()));
    }

    @Override
    public List<ProductMovement> findAll() {
        return persistencePort.findAll();
    }

    @Override
    public List<ProductMovement> findAllByOrderId(Long orderId) {
        return persistencePort.findAllByOrderId(orderId);
    }

    @Override
    public List<ProductMovement> findAllByProductId(Long productId) {
        return persistencePort.findAllByProductId(productId);
    }

    // Enviar evento al crear un movimiento
    @Override
    @Transactional
    public ProductMovement save(ProductMovement productMovement) {
        // Verifica si el producto es un plato
        if (productMovement.getProduct().getIsDish()) {
            throw new ConflictException("No se puede agregar un movimiento para un plato de comida.");
        }

        // Obtén el usuario desde el contexto de seguridad
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userServicePort.findByUsername(username);
        productMovement.setUser(user);

        ProductMovement savedMovement = persistencePort.save(productMovement);
        eventPublisher.publishEvent(new ProductMovementCreatedEvent(savedMovement));
        return savedMovement;
    }

    // Enviar evento al actualizar un movimiento
    @Override
    @Transactional
    public ProductMovement update(Long id, int newQuantity) {
        // Encontrar el movimiento existente
        ProductMovement existingMovement = persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.PRODUCT_MOVEMENT_NOT_FOUND.getMessage()));

        // Guardar la cantidad anterior antes de actualizar
        int oldQuantity = existingMovement.getQuantity();

        // Actualizar la cantidad del movimiento
        existingMovement.setQuantity(newQuantity);

        // Guardar el movimiento actualizado
        ProductMovement updatedMovement = persistencePort.save(existingMovement);

        // Crear un evento con la cantidad antigua y nueva
        ProductMovement oldMovement = new ProductMovement();
        oldMovement.setId(existingMovement.getId());
        oldMovement.setProduct(existingMovement.getProduct());
        oldMovement.setQuantity(oldQuantity);
        oldMovement.setMovementType(existingMovement.getMovementType());

        // Publicar el evento de actualización
        eventPublisher.publishEvent(new ProductMovementUpdatedEvent(oldMovement, updatedMovement));

        return updatedMovement;
    }

    // Enviar evento al eliminar un movimiento
    @Override
    @Transactional
    public void delete(Long id) {
        ProductMovement existingMovement = persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.PRODUCT_MOVEMENT_NOT_FOUND.getMessage()));

        persistencePort.delete(id);
        eventPublisher.publishEvent(new ProductMovementDeletedEvent(existingMovement));
    }

    @Transactional
    @Override
    public void deleteByOrderId(Long orderId) {
        persistencePort.deleteByOrderId(orderId);
    }
}
