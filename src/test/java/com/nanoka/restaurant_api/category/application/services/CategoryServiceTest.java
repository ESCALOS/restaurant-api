package com.nanoka.restaurant_api.category.application.services;

import com.nanoka.restaurant_api.category.application.ports.output.CategoryPersistencePort;
import com.nanoka.restaurant_api.category.domain.model.Category;
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
class CategoryServiceTest {

    @Mock
    private CategoryPersistencePort persistencePort;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void shouldThrowNotFoundExceptionWhenCategoryNotFound() {
        Long id = 1L;
        when(persistencePort.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> categoryService.findById(id));

        assertEquals(ErrorCatelog.CATEGORY_NOT_FOUND.getMessage(), exception.getMessage());
        verify(persistencePort).findById(id);
    }

    @Test
    void shouldSaveCategorySuccessfully() {
        Category category = new Category(null, "Beverages", "Description");
        when(persistencePort.findByName("Beverages")).thenReturn(Optional.empty());
        when(persistencePort.save(category)).thenReturn(category);

        Category savedCategory = categoryService.save(category);

        assertEquals("Beverages", savedCategory.getName());
        verify(persistencePort).findByName("Beverages");
        verify(persistencePort).save(category);
    }

    @Test
    void shouldThrowConflictExceptionWhenCategoryExists() {
        Category category = new Category(null, "Beverages", "Description");
        when(persistencePort.findByName("Beverages")).thenReturn(Optional.of(category));

        ConflictException exception = assertThrows(ConflictException.class, () -> categoryService.save(category));

        assertEquals(ErrorCatelog.CATEGORY_ALREADY_EXIST.getMessage(), exception.getMessage());
        verify(persistencePort).findByName("Beverages");
        verify(persistencePort, never()).save(any(Category.class));
    }

    @Test
    void shouldUpdateCategorySuccessfully() {
        Long id = 1L;
        Category existingCategory = new Category(id, "Beverages", "Description");
        Category updatedCategory = new Category(id, "Soft Drinks", "New Description");

        // Configurar simulación del puerto de persistencia
        when(persistencePort.findById(id)).thenReturn(Optional.of(existingCategory));
        when(persistencePort.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecutar el método de actualización en el servicio
        Category result = categoryService.update(id, updatedCategory);

        // Verificar que los valores editados coincidan
        assertEquals("Soft Drinks", result.getName());
        assertEquals(id, result.getId());

        // Verificar que se llamaron los métodos correctos
        verify(persistencePort).findById(id);
        verify(persistencePort).save(existingCategory);  // existingCategory se actualizará internamente con los datos de updatedCategory
    }


    @Test
    void shouldDeleteCategorySuccessfully() {
        Long id = 1L;
        Category category = new Category(id, "Beverages", "Description");

        // Configurar simulación del puerto de persistencia
        when(persistencePort.findById(id)).thenReturn(Optional.of(category));
        doNothing().when(persistencePort).deleteById(id);

        // Ejecutar el método de eliminación en el servicio
        categoryService.delete(id);

        // Verificar que se llamó a findById y deleteById correctamente
        verify(persistencePort).findById(id);
        verify(persistencePort).deleteById(id);
    }
}
