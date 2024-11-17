package com.paymentservice.validation;

import com.paymentservice.dto.InvoicePositionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InvoicePositionDtoValidatorTest {

    private InvoicePositionDtoValidator validator;
    private InvoicePositionDto invoicePositionDto;

    @BeforeEach
    void setUp() {
        invoicePositionDto = mock(InvoicePositionDto.class);
        when(invoicePositionDto.invoicePositionId()).thenReturn(1000);
        when(invoicePositionDto.invoicePositionDescription()).thenReturn("Product description");
        when(invoicePositionDto.amount()).thenReturn(BigDecimal.valueOf(123.45));

        validator = new InvoicePositionDtoValidator();
    }

    @Test
    void testValidate_SuccessfulValidation() {
        ValidationResult<List<InvoicePositionDto>> result = validator.validate(List.of(invoicePositionDto));

        assertTrue(result.isSuccess());
        assertFalse(result.getError().isPresent());
    }

    @Test
    void testValidate_InvalidId() {
        when(invoicePositionDto.invoicePositionId()).thenReturn(-1);

        ValidationResult<List<InvoicePositionDto>> result = validator.validate(List.of(invoicePositionDto));
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

    @Test
    void testValidate_DescriptionMissing() {
        when(invoicePositionDto.invoicePositionDescription()).thenReturn(null);

        ValidationResult<List<InvoicePositionDto>> result = validator.validate(List.of(invoicePositionDto));
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

    @Test
    void testValidate_DescriptionIsEmpty() {
        when(invoicePositionDto.invoicePositionDescription()).thenReturn("");

        ValidationResult<List<InvoicePositionDto>> result = validator.validate(List.of(invoicePositionDto));
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

    @Test
    void testValidate_InvoicePositionAmountNegative() {
        when(invoicePositionDto.amount()).thenReturn(BigDecimal.valueOf(-10));

        ValidationResult<List<InvoicePositionDto>> result = validator.validate(List.of(invoicePositionDto));
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

    @Test
    void testValidate_PositionAmountNull() {
        when(invoicePositionDto.amount()).thenReturn(null);

        ValidationResult<List<InvoicePositionDto>> result = validator.validate(List.of(invoicePositionDto));
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }
}
