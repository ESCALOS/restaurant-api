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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
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
    public void exportProductMovementsToExcel(String filePath) throws IOException {
        List<ProductMovement> productMovements = findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Product Movements");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Product ID");
        headerRow.createCell(2).setCellValue("Quantity");
        headerRow.createCell(3).setCellValue("Movement Type");
        headerRow.createCell(4).setCellValue("User ID");
        headerRow.createCell(5).setCellValue("Date");

        int rowNum = 1;
        for (ProductMovement movement : productMovements) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(movement.getId());
            row.createCell(1).setCellValue(movement.getProduct().getId());
            row.createCell(2).setCellValue(movement.getQuantity());
            row.createCell(3).setCellValue(movement.getMovementType().toString());
            row.createCell(4).setCellValue(movement.getUser().getId());
            row.createCell(5).setCellValue(movement.getDate().toString());
        }

        for (int i = 0; i < 6; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
}
