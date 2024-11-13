package com.paymentservice.validation;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.dto.InvoicePositionDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class InvoiceDtoValidator implements IValidator<InvoiceDto> {

    @Override
    public ValidationResult validate(InvoiceDto invoiceDto) {
        List<String> errors = new ArrayList<>();

        if (invoiceDto.invoiceDescription() == null || invoiceDto.invoiceDescription().isEmpty()) {
            errors.add("Invoice must have a description");
        }

        if (invoiceDto.positions() == null || invoiceDto.positions().isEmpty()) {
            errors.add("Invoice must have at least one position");
        } else {
            errors.addAll(validatePositions(invoiceDto.positions()));
        }

        return errors.isEmpty() ? ValidationResult.success() : ValidationResult.failure(errors);
    }

    private List<String> validatePositions(List<InvoicePositionDto> positions) {
        List<String> errors = new ArrayList<>();
        for (InvoicePositionDto position : positions) {
            if (position.amount() == null || position.amount().compareTo(BigDecimal.ZERO) <= 0) {
                errors.add("Position amount must be positive");
            }
        }
        return errors;
    }
}