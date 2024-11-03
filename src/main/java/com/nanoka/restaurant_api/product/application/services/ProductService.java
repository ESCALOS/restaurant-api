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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductServicePort {
    private final ProductPersistencePort persistencePort;

    @Override
    public Product findById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.PRODUCT_NOT_FOUND.getMessage()));
    }

    @Override
    public List<Product> findAll(Boolean isDish) {
        return persistencePort.findAll(isDish);
    }

    @Override
    public Product save(Product product, Boolean isDish) {
        persistencePort.findByName(product.getName())
                .ifPresent(p -> {
                    throw new ConflictException(ErrorCatelog.PRODUCT_ALREADY_EXIST.getMessage());
                });
        product.setIsDish(isDish);
        return persistencePort.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
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
                    existingProduct.setStock(product.getStock());
                    existingProduct.setMinStock(product.getMinStock());
                    return persistencePort.save(existingProduct);
                })
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.PRODUCT_NOT_FOUND.getMessage()));
    }

    @Override
    public void delete(Long id) {
        this.findById(id);
        persistencePort.deleteById(id);
    }

    @Override
    public Product modifyStock(Long id, Integer stock) {
        Product product = this.findById(id);
        product.setStock(stock + product.getStock());
        return persistencePort.save(product);
    }

    // Método para exportar productos a un archivo Excel
    public void exportProductsToExcel(String filePath) throws IOException {
        List<Product> products = findAll(false); // Si deseas incluir solo productos que no son platos

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Description");
        headerRow.createCell(3).setCellValue("Price");
        headerRow.createCell(4).setCellValue("Category");
        headerRow.createCell(5).setCellValue("Stock");
        headerRow.createCell(6).setCellValue("Min Stock");
        headerRow.createCell(7).setCellValue("Is Dish");

        int rowNum = 1;
        for (Product product : products) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getId());
            row.createCell(1).setCellValue(product.getName());
            row.createCell(2).setCellValue(product.getDescription());
            row.createCell(3).setCellValue(product.getPrice().doubleValue());
            row.createCell(4).setCellValue(product.getCategory().toString());
            row.createCell(5).setCellValue(product.getStock());
            row.createCell(6).setCellValue(product.getMinStock());
            row.createCell(7).setCellValue(product.getIsDish());
        }

        for (int i = 0; i < 8; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
}
