package com.nanoka.restaurant_api.table.infrastructure.adapters.output.persistence;

import com.nanoka.restaurant_api.table.infrastructure.adapters.output.persistence.mapper.TablePersistenceMapper;
import com.nanoka.restaurant_api.table.infrastructure.adapters.output.persistence.repository.TableRepository;
import com.nanoka.restaurant_api.table.application.ports.output.TablePersistencePort;
import com.nanoka.restaurant_api.table.domain.model.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TablePersistenceAdapter implements TablePersistencePort {
    private final TableRepository repository;
    private final TablePersistenceMapper mapper;


    @Override
    public Optional<Table> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toTable);
    }

    @Override
    public Optional<Table> findByNumber(String number) {
        return repository.findByNumber(number)
                .map(mapper::toTable);
    }

    @Override
    public List<Table> findAll() {
        return mapper.toTableList(repository.findAll());
    }

    @Override
    public Table save(Table Table) {
        return mapper.toTable(repository.save(mapper.toTableEntity(Table)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
