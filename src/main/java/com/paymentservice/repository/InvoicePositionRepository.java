package com.paymentservice.repository;

import com.paymentservice.entity.InvoicePositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoicePositionRepository extends JpaRepository<InvoicePositionEntity, Integer> {
    List<InvoicePositionEntity> findByInvoice_InvoiceId(Integer invoiceId);
}