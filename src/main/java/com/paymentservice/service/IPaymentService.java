package com.paymentservice.service;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.dto.PayerDto;

import java.util.List;
import java.util.Optional;

public interface IPaymentService {

    PayerDto createPayer(PayerDto payerDto);
    Optional getPayerById(Integer payerId);
    List<PayerDto> getAllPayers();
    PayerDto updatePayer(Integer payerId, PayerDto updatedPayer);
    List<InvoiceDto> getInvoicesByPayerId(Integer payerId);
}