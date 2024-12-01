package com.nanoka.restaurant_api.receipt.application.ports.output;

import com.nanoka.restaurant_api.receipt.model.Receipt;

import java.util.List;
import java.util.Optional;

public interface ReceiptPersistencePort {
    Optional<Receipt> findById(Long id);
    List<Receipt> findAll();
    Receipt save(Receipt category);
    void deleteById(Long id);
}
