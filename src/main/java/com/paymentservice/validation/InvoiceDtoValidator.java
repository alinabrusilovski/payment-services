package com.paymentservice.validation;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.dto.InvoicePositionDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class InvoiceDtoValidator {

    public void validate(InvoiceDto invoiceDto) {
        if (invoiceDto.invoiceDescription() == null || invoiceDto.invoiceDescription().isEmpty()) {
            throw new IllegalArgumentException("Invoice must have a description.");
        }

        if (invoiceDto.positions() == null || invoiceDto.positions().isEmpty()) {
            throw new IllegalArgumentException("Invoice must have at least one position.");
        }

        validatePositions(invoiceDto.positions());
    }

    private void validatePositions(List<InvoicePositionDto> positions) {
        for (InvoicePositionDto position : positions) {
            if (position.amount() == null || position.amount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Position amount must be positive.");
            }
        }
    }
}