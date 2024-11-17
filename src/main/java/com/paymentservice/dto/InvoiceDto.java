package com.paymentservice.dto;

import org.springframework.lang.Nullable;

import java.util.List;

public record InvoiceDto(@Nullable Integer systemId, String invoiceDescription, PayerDto payer,
                         List<InvoicePositionDto> positions) {
}