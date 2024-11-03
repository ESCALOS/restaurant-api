package com.nanoka.restaurant_api.table.application.ports.input;

import com.nanoka.restaurant_api.table.domain.model.Table;

import java.util.List;

public interface TableServicePort {
    Table findById(Long id);
    List<Table> findAll();
    Table save(Table table);
    Table update(Long id, Table table);
    void delete(Long id);
    void toggleEnabled(Long id, Boolean isEnabled);
}
