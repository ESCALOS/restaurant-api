package com.nanoka.restaurant_api.category.infrastructure.adapters.output.persistence;

import com.nanoka.restaurant_api.category.application.ports.output.CategoryPersistencePort;
import com.nanoka.restaurant_api.category.domain.model.Category;
import com.nanoka.restaurant_api.category.infrastructure.adapters.output.persistence.mapper.CategoryPersistenceMapper;
import com.nanoka.restaurant_api.category.infrastructure.adapters.output.persistence.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryPersistencePort {
    private final CategoryRepository repository;
    private final CategoryPersistenceMapper mapper;


    @Override
    public Optional<Category> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toCategory);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return repository.findByName(name)
                .map(mapper::toCategory);
    }

    @Override
    public List<Category> findAll() {
        return mapper.toCategoryList(repository.findAll());
    }

    @Override
    public Category save(Category category) {
        return mapper.toCategory(repository.save(mapper.toCategoryEntity(category)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
