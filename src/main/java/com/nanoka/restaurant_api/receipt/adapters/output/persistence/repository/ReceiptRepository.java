package com.nanoka.restaurant_api.receipt.adapters.output.persistence.repository;

import com.nanoka.restaurant_api.receipt.adapters.output.persistence.entity.ReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<ReceiptEntity, Long> { }
