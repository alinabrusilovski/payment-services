package com.paymentservice.service;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.dto.JsonWrapper;
import com.paymentservice.dto.OperationResult;
import com.paymentservice.entity.InvoiceEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPaymentService {
    OperationResult<InvoiceEntity> createInvoice(InvoiceDto invoiceDto);

    OperationResult<JsonWrapper<List<InvoiceEntity>>> getAllInvoices(int offset, int limit, String sortBy, String sortDirection);
}