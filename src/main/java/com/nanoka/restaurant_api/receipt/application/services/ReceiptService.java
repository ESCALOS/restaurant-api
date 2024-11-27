package com.nanoka.restaurant_api.receipt.application.services;

import com.nanoka.restaurant_api.receipt.application.ports.input.ReceiptServicePort;
import com.nanoka.restaurant_api.receipt.application.ports.output.ReceiptPersistencePort;
import com.nanoka.restaurant_api.receipt.model.Receipt;
import com.nanoka.restaurant_api.util.ErrorCatelog;
import com.nanoka.restaurant_api.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptService implements ReceiptServicePort {

    private static final Logger logger = LoggerFactory.getLogger(ReceiptService.class);
    private final ReceiptPersistencePort persistencePort;

    @Override
    public Receipt findById(Long id) {
        logger.info("Buscando recibo con id: {}", id);
        return persistencePort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCatelog.RECEIPT_NOT_FOUND.getMessage()));
    }

    @Override
    public List<Receipt> findAll() {
        logger.info("Buscando todos los recibos");
        return persistencePort.findAll();
    }
}
