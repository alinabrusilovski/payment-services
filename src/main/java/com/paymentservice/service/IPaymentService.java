package com.paymentservice.service;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.dto.InvoicePositionDto;
import com.paymentservice.dto.PayerDto;
import com.paymentservice.entity.InvoiceEntity;
import com.paymentservice.entity.InvoicePositionEntity;

import java.util.List;
import java.util.Optional;

public interface IPaymentService {
    InvoiceEntity createInvoice(InvoiceDto invoiceDto);
    List<InvoiceEntity> getAllInvoices();
//    PayerDto createPayer(PayerDto payerDto);
//    Optional getPayerById(Integer payerId);
//    List<PayerDto> getAllPayers();
//    PayerDto updatePayer(Integer payerId, PayerDto updatedPayer);
//    List<InvoiceDto> getInvoicesByPayerId(Integer payerId);
//    InvoicePositionDto createInvoicePosition(InvoicePositionDto positionDto, Integer invoiceId);
//    List<InvoicePositionDto> getInvoicePositionsByInvoiceId(Integer invoiceId);
//    InvoicePositionDto getInvoicePositionById(Integer positionId);
//    InvoicePositionDto updateInvoicePosition(Integer positionId, InvoicePositionDto positionDto);
//    void deleteInvoicePosition(Integer positionId);
}