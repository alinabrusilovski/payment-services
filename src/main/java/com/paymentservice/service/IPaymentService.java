package com.paymentservice.service;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.entity.InvoiceEntity;
import com.paymentservice.validation.ValidationResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPaymentService {
    ValidationResult<InvoiceEntity> createInvoice(InvoiceDto invoiceDto);

    ValidationResult<List<InvoiceEntity>> getAllInvoices();
}