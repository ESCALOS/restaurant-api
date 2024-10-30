package com.nanoka.restaurant_api.product.application.services;

import com.nanoka.restaurant_api.product.application.ports.output.ProductPersistencePort;
import com.nanoka.restaurant_api.product.domain.model.Product;
import com.nanoka.restaurant_api.util.ErrorCatelog;
import com.nanoka.restaurant_api.util.exceptions.ConflictException;
import com.nanoka.restaurant_api.util.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductPersistencePort persistencePort;

    @InjectMocks
    private ProductService productService;

    @Test
    void findById_whenProductExists_shouldReturnProduct() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        when(persistencePort.findById(1L)).thenReturn(Optional.of(product));

        // Act
        Product foundProduct = productService.findById(1L);

        // Assert
        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getId());
        verify(persistencePort, times(1)).findById(1L);
    }

    @Test
    void findById_whenProductDoesNotExist_shouldThrowNotFoundException() {
        Long id = 1L;
        when(persistencePort.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> productService.findById(id));
        assertEquals(ErrorCatelog.PRODUCT_NOT_FOUND.getMessage(), exception.getMessage());
        verify(persistencePort).findById(id);
    }

    @Test
    void save_whenProductNameExists_shouldThrowConflictException() {
        // Arrange
        Product product = new Product();
        product.setName("Existing Product");
        when(persistencePort.findByName("Existing Product")).thenReturn(Optional.of(product));

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> productService.save(product, true));
        assertEquals(ErrorCatelog.PRODUCT_ALREADY_EXIST.getMessage(), exception.getMessage());
        verify(persistencePort, never()).save(any(Product.class));
    }

    @Test
    void save_whenProductIsValid_shouldSaveProduct() {
        // Arrange
        Product product = new Product();
        product.setName("New Product");
        product.setIsDish(true);
        when(persistencePort.findByName("New Product")).thenReturn(Optional.empty());
        when(persistencePort.save(product)).thenReturn(product);

        // Act
        Product savedProduct = productService.save(product, true);

        // Assert
        assertNotNull(savedProduct);
        assertTrue(savedProduct.getIsDish());
        verify(persistencePort, times(1)).save(product);
    }

    @Test
    void update_whenProductExists_shouldUpdateProduct() {
        // Arrange
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Existing Product");

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");

        when(persistencePort.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(persistencePort.findByName("Updated Product")).thenReturn(Optional.empty());
        when(persistencePort.save(any(Product.class))).thenReturn(updatedProduct);

        // Act
        Product result = productService.update(1L, updatedProduct);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        verify(persistencePort, times(1)).save(any(Product.class));
    }

    @Test
    void update_whenProductDoesNotExist_shouldThrowNotFoundException() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Non-existing Product");

        when(persistencePort.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> productService.update(1L, product));
        assertEquals(ErrorCatelog.PRODUCT_NOT_FOUND.getMessage(), exception.getMessage());
        verify(persistencePort, never()).save(any(Product.class));
    }

    @Test
    void delete_whenProductExists_shouldDeleteProduct() {
        // Arrange
        Product product = new Product();
        product.setId(1L);

        when(persistencePort.findById(1L)).thenReturn(Optional.of(product));

        // Act
        productService.delete(1L);

        // Assert
        verify(persistencePort, times(1)).deleteById(1L);
    }

    @Test
    void delete_whenProductDoesNotExist_shouldThrowNotFoundException() {
        // Arrange
        when(persistencePort.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> productService.delete(1L));
        assertEquals(ErrorCatelog.PRODUCT_NOT_FOUND.getMessage(), exception.getMessage());
        verify(persistencePort, never()).deleteById(anyLong());
    }

    @Test
    void modifyStock_whenProductExists_shouldUpdateStock() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setStock(50);

        when(persistencePort.findById(1L)).thenReturn(Optional.of(product));
        when(persistencePort.save(product)).thenReturn(product);

        // Act
        Product result = productService.modifyStock(1L, 10);

        // Assert
        assertEquals(60, result.getStock(), "Stock should be updated by the specified amount");
        verify(persistencePort, times(1)).save(product);
    }

    @Test
    void modifyStock_whenProductDoesNotExist_shouldThrowNotFoundException() {
        // Arrange
        when(persistencePort.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> productService.modifyStock(1L, 10));
        assertEquals(ErrorCatelog.PRODUCT_NOT_FOUND.getMessage(), exception.getMessage());
        verify(persistencePort, never()).save(any(Product.class));
    }
}
