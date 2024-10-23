package com.paymentservice.dto;

import java.util.List;

public record InvoiceDto(Integer systemId, String invoiceDescription, PayerDto payer,
                         List<InvoicePositionDto> positions) {
}