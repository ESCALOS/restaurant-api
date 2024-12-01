package com.nanoka.restaurant_api.receipt.adapters.output.persistence;

import com.nanoka.restaurant_api.receipt.adapters.output.persistence.mapper.ReceiptPersistenceMapper;
import com.nanoka.restaurant_api.receipt.adapters.output.persistence.repository.ReceiptRepository;
import com.nanoka.restaurant_api.receipt.application.ports.output.ReceiptPersistencePort;
import com.nanoka.restaurant_api.receipt.model.Receipt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReceiptPersistenceAdapter implements ReceiptPersistencePort {
    private final ReceiptRepository repository;
    private final ReceiptPersistenceMapper mapper;


    @Override
    public Optional<Receipt> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toReceipt);
    }

    @Override
    public List<Receipt> findAll() {
        return mapper.toReceiptList(repository.findAll());
    }

    @Override
    public Receipt save(Receipt receipt) {
        return mapper.toReceipt(repository.save(mapper.toReceiptEntity(receipt)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
