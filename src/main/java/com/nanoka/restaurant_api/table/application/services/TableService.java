package com.nanoka.restaurant_api.table.application.services;

import com.nanoka.restaurant_api.table.application.ports.input.TableServicePort;
import com.nanoka.restaurant_api.table.application.ports.output.TablePersistencePort;
import com.nanoka.restaurant_api.table.domain.model.Table;
import com.nanoka.restaurant_api.util.ErrorCatelog;
import com.nanoka.restaurant_api.util.exceptions.ConflictException;
import com.nanoka.restaurant_api.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService implements TableServicePort {

    private final TablePersistencePort persistencePort;

    @Override
    public Table findById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.TABLE_NOT_FOUND.getMessage()));
    }

    @Override
    public List<Table> findAll() { return persistencePort.findAll(); }

    @Override
    public Table save(Table table) {
        persistencePort.findByNumber(table.getNumber())
                .ifPresent(c-> {
                    throw new ConflictException(ErrorCatelog.TABLE_ALREADY_EXIST.getMessage());
                });

        table.setIsAvailable(true);

        return persistencePort.save(table);
    }



    @Override
    public Table update(Long id, Table table) {
        return persistencePort.findById(id)
                .map(existingTable -> {
                    persistencePort.findByNumber(table.getNumber())
                            .filter(c -> !c.getId().equals(id))
                            .ifPresent(c -> {
                                throw new ConflictException(ErrorCatelog.TABLE_ALREADY_EXIST.getMessage());
                            });
                    existingTable.setNumber(table.getNumber());
                    return persistencePort.save(existingTable);
                })
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.TABLE_NOT_FOUND.getMessage()));
    }

    @Override
    public void delete(Long id) {
        persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.TABLE_NOT_FOUND.getMessage()));

        persistencePort.deleteById(id);
    }

    @Override
    public void toggleEnabled(Long id, Boolean isEnabled) {
        Table table = this.findById(id);

        table.setIsAvailable(isEnabled);

        persistencePort.save(table);
    }
}
