package com.paymentservice.validation;

import com.paymentservice.dto.InvoicePositionDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class InvoicePositionDtoValidator implements IValidator<List<InvoicePositionDto>> {

    @Override
    public ValidationResult<List<InvoicePositionDto>> validate(List<InvoicePositionDto> positions) {

        if (positions == null || positions.isEmpty()) {
            return ValidationResult.failure("Invoice must have at least one position");
        }
        for (InvoicePositionDto position : positions) {
            if (position.amount() == null || position.amount().compareTo(BigDecimal.ZERO) <= 0) {
                return ValidationResult.failure("Position amount must be positive");
            }
        }

        return ValidationResult.success(positions);
    }
}
