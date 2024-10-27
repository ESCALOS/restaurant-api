package com.nanoka.restaurant_api.table.application.ports.output;

import com.nanoka.restaurant_api.table.domain.model.Table;

import java.util.List;
import java.util.Optional;

public interface TablePersistencePort {
    Optional<Table> findById(Long id);
    Optional<Table> findByNumber(String number);
    List<Table> findAll();
    Table save(Table Table);
    void deleteById(Long id);
}
