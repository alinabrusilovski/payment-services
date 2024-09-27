package com.paymentservice.repository;

import com.paymentservice.dto.InvoicePositionDto;
import com.paymentservice.entity.InvoicePositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoicePositionRepository extends JpaRepository<InvoicePositionEntity, Long> {
//    List<InvoicePositionEntity> findByInvoice_InvoiceId(Long invoiceId); // Для получения позиций по инвойсу
}