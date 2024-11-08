package com.paymentservice.validation;

import com.paymentservice.dto.InvoicePositionDto;

import java.math.BigDecimal;
import java.util.List;

public class InvoicePositionDtoValidator {
    public void validatePositions(List<InvoicePositionDto> positions) {
        if (positions == null || positions.isEmpty()) {
            throw new IllegalArgumentException("Invoice must have at least one position.");
        }

        for (InvoicePositionDto position : positions) {
            if (position.amount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Position amount must be positive.");
            }
        }
    }
}
