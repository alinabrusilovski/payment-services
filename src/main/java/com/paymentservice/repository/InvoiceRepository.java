package com.paymentservice.repository;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {
}