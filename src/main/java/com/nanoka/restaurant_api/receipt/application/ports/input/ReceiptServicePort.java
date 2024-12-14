package com.nanoka.restaurant_api.receipt.application.ports.input;

import com.nanoka.restaurant_api.receipt.domain.model.Receipt;

import java.util.List;

import com.nanoka.restaurant_api.receipt.infrastructure.adapters.input.rest.model.request.ReceiptCreateRequest;

public interface ReceiptServicePort {
    Receipt findById(Long id);
    List<Receipt> findAll();
    Receipt save(ReceiptCreateRequest request);

}
