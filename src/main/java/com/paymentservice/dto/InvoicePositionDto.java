package com.paymentservice.dto;

import jakarta.validation.constraints.Digits;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;

public record InvoicePositionDto(@Nullable Integer invoicePositionId, String invoicePositionDescription,
                                 @Digits(integer = 100, fraction = 2) BigDecimal amount) {
}