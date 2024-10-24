package com.nanoka.restaurant_api.category.application.services;

import com.nanoka.restaurant_api.category.application.ports.input.CategoryServicePort;
import com.nanoka.restaurant_api.category.application.ports.output.CategoryPersistencePort;
import com.nanoka.restaurant_api.category.domain.model.Category;
import com.nanoka.restaurant_api.util.ErrorCatelog;
import com.nanoka.restaurant_api.util.exceptions.ConflictException;
import com.nanoka.restaurant_api.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServicePort {

    private final CategoryPersistencePort persistencePort;

    @Override
    public Category findById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.CATEGORY_NOT_FOUND.getMessage()));
    }

    @Override
    public List<Category> findAll() { return persistencePort.findAll(); }

    @Override
    public Category save(Category category) {
        persistencePort.findByName(category.getName())
                .ifPresent(c-> {
                    throw new ConflictException(ErrorCatelog.CATEGORY_ALREADY_EXIST.getMessage());
                });

        return persistencePort.save(category);
    }

    @Override
    public Category update(Long id,Category category) {
        return persistencePort.findById(id)
                .map(existingCategory -> {
                    persistencePort.findByName(category.getName())
                            .filter(c -> !c.getId().equals(id))
                            .ifPresent(c -> {
                                throw new ConflictException(ErrorCatelog.CATEGORY_ALREADY_EXIST.getMessage());
                            });
                    existingCategory.setName(category.getName());
                    return persistencePort.save(existingCategory);
                })
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.CATEGORY_NOT_FOUND.getMessage()));
    }

    @Override
    public void delete(Long id) {
        persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.CATEGORY_NOT_FOUND.getMessage()));

        persistencePort.deleteById(id);
    }
}
