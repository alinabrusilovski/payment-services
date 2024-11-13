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

        assertFalse(result.valid());
        assertEquals("Position amount must be positive", result.errorMessage().get(0));
    }

    @Test
    void testValidate_PositionAmountNull() {
        InvoicePositionDto position = mock(InvoicePositionDto.class);
        when(position.amount()).thenReturn(null);

        ValidationResult result = validator.validate(List.of(position));

        assertFalse(result.valid());
        assertTrue(result.errorMessage().contains("Position amount must be positive"));
    }

    @Test
    void testValidate_PositionAmountZero() {
        InvoicePositionDto position = mock(InvoicePositionDto.class);
        when(position.amount()).thenReturn(BigDecimal.ZERO);

        ValidationResult result = validator.validate(List.of(position));

        assertFalse(result.valid());
        assertEquals("Position amount must be positive", result.errorMessage().get(0));
    }

    @Test
    void testValidate_PositionsWithMultipleErrors() {
        InvoicePositionDto positionWithNegativeAmount = mock(InvoicePositionDto.class);
        when(positionWithNegativeAmount.amount()).thenReturn(new BigDecimal("-10"));

        InvoicePositionDto positionWithNullAmount = mock(InvoicePositionDto.class);
        when(positionWithNullAmount.amount()).thenReturn(null);

        ValidationResult result = validator.validate(List.of(positionWithNegativeAmount, positionWithNullAmount));

        assertFalse(result.valid());
        assertEquals(List.of(
                "Position amount must be positive",
                "Position amount must be positive"
        ), result.errorMessage());
    }

    @Test
    void testValidate_PositionsNull() {
        ValidationResult result = validator.validate(null);

        assertFalse(result.valid());
        assertTrue(result.errorMessage().contains("Invoice must have at least one position"));
    }

    @Test
    void testValidate_PositionsEmpty() {
        ValidationResult result = validator.validate(List.of());

        assertFalse(result.valid());
        assertTrue(result.errorMessage().contains("Invoice must have at least one position"));
    }

    @Test
    void testValidate_ValidPositions() {
        InvoicePositionDto position = mock(InvoicePositionDto.class);
        when(position.amount()).thenReturn(new BigDecimal("10.00"));

        ValidationResult result = validator.validate(List.of(position));

        assertTrue(result.valid());
    }

}
