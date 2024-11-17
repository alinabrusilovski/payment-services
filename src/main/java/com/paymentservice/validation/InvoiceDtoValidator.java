package com.paymentservice.validation;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.dto.InvoicePositionDto;
import com.paymentservice.dto.PayerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InvoiceDtoValidator implements IValidator<InvoiceDto> {

    private final PayerDtoValidator payerDtoValidator;
    private final InvoicePositionDtoValidator invoicePositionDtoValidator;

    @Autowired
    public InvoiceDtoValidator(
            PayerDtoValidator payerDtoValidator,
            InvoicePositionDtoValidator invoicePositionDtoValidator
    ) {
        this.payerDtoValidator = payerDtoValidator;
        this.invoicePositionDtoValidator = invoicePositionDtoValidator;
    }

    @Override
    public ValidationResult<InvoiceDto> validate(InvoiceDto invoiceDto) {
        List<String> errors = new ArrayList<>();

        if (invoiceDto == null)
            return ValidationResult.failure("Invoice cannot be null");

        if(invoiceDto.systemId() != null && invoiceDto.systemId() <= 0)
            return ValidationResult.failure("Invoice id must be positive");

        if (invoiceDto.payer() == null)
            return ValidationResult.failure("Invoice must have a payer");

        ValidationResult<PayerDto> payerValidation = payerDtoValidator.validate(invoiceDto.payer());

        if (!payerValidation.isSuccess())
            return ValidationResult.failure(payerValidation.getError().orElse("Unknown error in payer validation"));

        if (invoiceDto.positions() == null)
            return ValidationResult.failure("Invoice must have at least one position");

        if (invoiceDto.invoiceDescription() == null || invoiceDto.invoiceDescription().isEmpty())
            return ValidationResult.failure("Invoice must have a description");

        ValidationResult<List<InvoicePositionDto>> positionValidation = invoicePositionDtoValidator.validate(invoiceDto.positions());

        if (!positionValidation.isSuccess())
            return ValidationResult.failure(positionValidation.getError().orElse("Unknown error in position validation"));

        return ValidationResult.success(invoiceDto);
    }

}