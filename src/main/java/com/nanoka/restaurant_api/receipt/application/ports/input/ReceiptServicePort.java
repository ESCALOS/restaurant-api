package com.nanoka.restaurant_api.receipt.application.ports.input;

import com.nanoka.restaurant_api.receipt.domain.model.Receipt;

import java.util.List;

public interface ReceiptServicePort {
    Receipt findById(Long id);
    List<Receipt> findAll();
}
