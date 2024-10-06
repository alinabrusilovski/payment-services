package com.paymentservice.dto;

public record InvoicePositionDto(Integer invoicePositionId, String invoicePositionDescription, Double amount) {
}