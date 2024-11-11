package com.paymentservice.validation;

import com.paymentservice.dto.InvoicePositionDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class InvoicePositionDtoValidator implements IValidator<List<InvoicePositionDto>> {

    @Override
    public ValidationResult validate(List<InvoicePositionDto> positions) {
        List<String> errors = new ArrayList<>();

        if (positions == null || positions.isEmpty()) {
            errors.add("Invoice must have at least one position.");
        } else {
            for (InvoicePositionDto position : positions) {
                if (position.amount() == null || position.amount().compareTo(BigDecimal.ZERO) <= 0) {
                    errors.add("Position amount must be positive.");
                }
            }
        }

        return errors.isEmpty() ? ValidationResult.success() : ValidationResult.failure(errors);
    }
}
