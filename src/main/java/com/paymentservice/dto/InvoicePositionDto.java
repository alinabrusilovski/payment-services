package com.paymentservice.dto;

public record InvoicePositionDto(Long invoicePositionId, Long invoiceId, String invoicePositionDescription, Double amount) {
}