package com.paymentservice.service;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.dto.InvoicePositionDto;
import com.paymentservice.dto.PayerDto;
import com.paymentservice.entity.InvoiceEntity;
import com.paymentservice.entity.InvoicePositionEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IPaymentService {
    InvoiceEntity createInvoice(InvoiceDto invoiceDto);
    List<InvoiceEntity> getAllInvoices();
}