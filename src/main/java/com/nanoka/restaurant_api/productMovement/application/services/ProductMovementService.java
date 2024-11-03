package com.nanoka.restaurant_api.productMovement.application.services;

import com.nanoka.restaurant_api.productMovement.application.events.ProductMovementCreatedEvent;
import com.nanoka.restaurant_api.productMovement.application.events.ProductMovementDeletedEvent;
import com.nanoka.restaurant_api.productMovement.application.events.ProductMovementUpdatedEvent;
import com.nanoka.restaurant_api.productMovement.application.ports.input.ProductMovementServicePort;
import com.nanoka.restaurant_api.productMovement.application.ports.output.ProductMovementPersistencePort;
import com.nanoka.restaurant_api.productMovement.domain.model.MovementTypeEnum;
import com.nanoka.restaurant_api.productMovement.domain.model.ProductMovement;
import com.nanoka.restaurant_api.user.application.ports.input.UserServicePort;
import com.nanoka.restaurant_api.user.domain.model.User;
import com.nanoka.restaurant_api.util.ErrorCatelog;
import com.nanoka.restaurant_api.util.exceptions.ConflictException;
import com.nanoka.restaurant_api.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
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
        return persistencePort.findByOrderIdAndProductId(orderId, productId)
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

    @Override
    @Transactional
    public ProductMovement save(ProductMovement productMovement) {
        if (productMovement.getProduct().getIsDish()) {
            throw new ConflictException("No se puede agregar un movimiento para un plato de comida.");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userServicePort.findByUsername(username);
        productMovement.setUser(user);

        ProductMovement savedMovement = persistencePort.save(productMovement);
        eventPublisher.publishEvent(new ProductMovementCreatedEvent(savedMovement));
        return savedMovement;
    }

    @Override
    @Transactional
    public ProductMovement update(Long id, int newQuantity) {
        ProductMovement existingMovement = persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.PRODUCT_MOVEMENT_NOT_FOUND.getMessage()));

        int oldQuantity = existingMovement.getQuantity();
        existingMovement.setQuantity(newQuantity);

        ProductMovement updatedMovement = persistencePort.save(existingMovement);

        ProductMovement oldMovement = new ProductMovement();
        oldMovement.setId(existingMovement.getId());
        oldMovement.setProduct(existingMovement.getProduct());
        oldMovement.setQuantity(oldQuantity);
        oldMovement.setMovementType(existingMovement.getMovementType());

        eventPublisher.publishEvent(new ProductMovementUpdatedEvent(oldMovement, updatedMovement));

        return updatedMovement;
    }

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

    // Método para exportar todos los movimientos de productos a un archivo Excel
    @Override
    public byte[] exportProductsToExcel() throws IOException {
        List<ProductMovement> productMovements = findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Movimiento de Productos");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Producto");
            headerRow.createCell(2).setCellValue("Cantidad");
            headerRow.createCell(3).setCellValue("Tipo de Movimiento");
            headerRow.createCell(4).setCellValue("Usuario");
            headerRow.createCell(5).setCellValue("Fecha");

            int rowNum = 1;
            for (ProductMovement movement : productMovements) {
                String productMovementType = movement.getMovementType() == MovementTypeEnum.INPUT ? "Ingreso": "Salida";
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 1);
                row.createCell(1).setCellValue(movement.getProduct().getName());
                row.createCell(2).setCellValue(movement.getQuantity());
                row.createCell(3).setCellValue(productMovementType);
                row.createCell(4).setCellValue(movement.getUser().getName());
                row.createCell(5).setCellValue(LocalDateTime.now().toLocalDate().toString());
            }

            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }

            // Escribir los datos en el output stream y devolver los bytes
            workbook.write(out);
            return out.toByteArray();
        }
    }
}
