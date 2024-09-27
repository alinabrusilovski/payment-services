package com.paymentservice.service;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.dto.PayerDto;

import java.util.List;
import java.util.Optional;

public interface IPayerService {

    PayerDto createPayer(PayerDto payerDto);
    Optional getPayerById(Long payerId);
    List<PayerDto> getAllPayers();
    PayerDto updatePayer(Long payerId, PayerDto updatedPayer);
    List<InvoiceDto> getInvoicesByPayerId(Long payerId);
}