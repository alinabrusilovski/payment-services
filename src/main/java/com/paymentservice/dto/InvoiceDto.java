package com.paymentservice.dto;

public record InvoiceDto(Long invoiceId, Long systemId, Long payerId, String invoiceDescription) {
}