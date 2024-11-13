package com.paymentservice.validation;

import com.paymentservice.dto.InvoicePositionDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InvoicePositionDtoValidatorTest {

    private final InvoicePositionDtoValidator validator = new InvoicePositionDtoValidator();

    @Test
    void testValidate_InvoicePositionAmountNegative() {
        InvoicePositionDto position = mock(InvoicePositionDto.class);
        when(position.amount()).thenReturn(new BigDecimal("-10"));

        ValidationResult result = validator.validate(List.of(position));

        assertFalse(result.isValid());
        assertEquals("Position amount must be positive", result.getErrorMessage().get(0));
    }

    @Test
    void testValidate_PositionAmountNull() {
        InvoicePositionDto position = mock(InvoicePositionDto.class);
        when(position.amount()).thenReturn(null);

        ValidationResult result = validator.validate(List.of(position));

        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("Position amount must be positive"));
    }

    @Test
    void testValidate_PositionAmountZero() {
        InvoicePositionDto position = mock(InvoicePositionDto.class);
        when(position.amount()).thenReturn(BigDecimal.ZERO);

        ValidationResult result = validator.validate(List.of(position));

        assertFalse(result.isValid());
        assertEquals("Position amount must be positive", result.getErrorMessage().get(0));
    }

    @Test
    void testValidate_PositionsNull() {
        ValidationResult result = validator.validate(null);

        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("Invoice must have at least one position"));
    }

    @Test
    void testValidate_PositionsEmpty() {
        ValidationResult result = validator.validate(List.of());

        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("Invoice must have at least one position"));
    }

    @Test
    void testValidate_ValidPositions() {
        InvoicePositionDto position = mock(InvoicePositionDto.class);
        when(position.amount()).thenReturn(new BigDecimal("10.00"));

        ValidationResult result = validator.validate(List.of(position));

        assertTrue(result.isValid());
    }

}