package com.paymentservice.dto;

import org.springframework.lang.Nullable;

import java.math.BigDecimal;

public record InvoicePositionDto(@Nullable Integer invoicePositionId, String invoicePositionDescription, BigDecimal amount) {
}