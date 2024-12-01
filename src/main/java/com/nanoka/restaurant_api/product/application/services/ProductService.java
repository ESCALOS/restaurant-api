package com.nanoka.restaurant_api.product.application.services;

import com.nanoka.restaurant_api.product.application.ports.output.ProductPersistencePort;
import com.nanoka.restaurant_api.product.domain.model.Product;
import com.nanoka.restaurant_api.product.application.ports.input.ProductServicePort;
import com.nanoka.restaurant_api.util.ErrorCatelog;
import com.nanoka.restaurant_api.util.exceptions.ConflictException;
import com.nanoka.restaurant_api.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductServicePort {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductPersistencePort persistencePort;

    @Override
    public Product findById(Long id) {
        logger.info("Buscando producto con id: {}", id);
        return persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.PRODUCT_NOT_FOUND.getMessage()));
    }

    @Override
    public List<Product> findAll(Boolean isDish) {
        logger.info("Obteniendo todos los productos, isDish: {}", isDish);
        return persistencePort.findAll(isDish);
    }

    @Override
    public Product save(Product product, Boolean isDish) {
        logger.info("Guardando nuevo producto: {}", product.getName());
        persistencePort.findByName(product.getName())
                .ifPresent(p -> {
                    throw new ConflictException(ErrorCatelog.PRODUCT_ALREADY_EXIST.getMessage());
                });
        product.setStock(0);
        product.setIsDish(isDish);
        return persistencePort.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        logger.info("Actualizando producto con id: {}", id);
        return persistencePort.findById(id)
                .map(existingProduct -> {
                    persistencePort.findByName(product.getName())
                        .filter(p -> !p.getId().equals(id))
                        .ifPresent(p -> {
                            throw new ConflictException(ErrorCatelog.PRODUCT_ALREADY_EXIST.getMessage());
                        });
                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setImageUrl(product.getImageUrl());
                    existingProduct.setCategory(product.getCategory());
                    existingProduct.setMinStock(product.getMinStock());
                    return persistencePort.save(existingProduct);
                })
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.PRODUCT_NOT_FOUND.getMessage()));
    }

    @Override
    public void delete(Long id) {
        logger.info("Eliminando producto con id: {}", id);
        this.findById(id);
        persistencePort.deleteById(id);
    }

    @Override
    public Product modifyStock(Long id, Integer stock) {
        logger.info("Modificando stock del producto con id: {}", id);
        Product product = this.findById(id);
        product.setStock(stock + product.getStock());
        return persistencePort.save(product);
    }

    @Override
    public byte[] exportProductsToExcel() throws IOException {
        logger.info("Exportando productos a Excel");
        List<Product> products = findAll(false);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Products");

            // Crear encabezado
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Nombre");
            headerRow.createCell(2).setCellValue("Descripción");
            headerRow.createCell(3).setCellValue("Precio");
            headerRow.createCell(4).setCellValue("Categoría");
            headerRow.createCell(5).setCellValue("Stock");
            headerRow.createCell(6).setCellValue("Stock Mínimo");

            // Crear filas con los datos de productos
            int rowNum = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 1);
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getDescription());
                row.createCell(3).setCellValue(product.getPrice().doubleValue());
                row.createCell(4).setCellValue(product.getCategory().getName());
                row.createCell(5).setCellValue(product.getStock());
                row.createCell(6).setCellValue(product.getMinStock());
            }

            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
            }

            // Escribir los datos en el output stream y devolver los bytes
            workbook.write(out);
            return out.toByteArray();
        }
    }
}
