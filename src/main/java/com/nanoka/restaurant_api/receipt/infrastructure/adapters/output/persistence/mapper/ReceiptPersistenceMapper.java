package com.nanoka.restaurant_api.receipt.infrastructure.adapters.output.persistence.mapper;

import com.nanoka.restaurant_api.receipt.infrastructure.adapters.output.persistence.entity.ReceiptEntity;
import com.nanoka.restaurant_api.receipt.domain.model.Receipt;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReceiptPersistenceMapper {
    ReceiptEntity toReceiptEntity(Receipt receipt);
    Receipt toReceipt(ReceiptEntity entity);
    List<Receipt> toReceiptList(List<ReceiptEntity> receiptList);
}
