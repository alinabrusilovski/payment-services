package com.paymentservice.validation;

import com.paymentservice.dto.InvoicePositionDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class InvoicePositionDtoValidator implements IValidator<List<InvoicePositionDto>> {

    @Override
    public ValidationResult<List<InvoicePositionDto>> validate(List<InvoicePositionDto> positions) {

        for (InvoicePositionDto position : positions) {
            if (position.invoicePositionId() != null && position.invoicePositionId() <= 0)
                return ValidationResult.failure("Position id must be positive");
            if (position.invoicePositionDescription() == null || position.invoicePositionDescription().isEmpty())
                return ValidationResult.failure("Position must have a description");
            if (position.amount() == null || position.amount().compareTo(BigDecimal.ZERO) <= 0)
                return ValidationResult.failure("Position amount must be positive");
        }

        return ValidationResult.success(positions);
    }
}
