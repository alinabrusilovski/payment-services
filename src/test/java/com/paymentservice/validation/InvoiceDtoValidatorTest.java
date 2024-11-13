package com.paymentservice.validation;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.dto.InvoicePositionDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InvoiceDtoValidatorTest {

    private final InvoiceDtoValidator validator = new InvoiceDtoValidator();

    @Test
    void testValidate_InvoiceDescriptionMissing() {
        InvoiceDto invoiceDto = mock(InvoiceDto.class);
        when(invoiceDto.invoiceDescription()).thenReturn(null);

        ValidationResult result = validator.validate(invoiceDto);

        assertFalse(result.isValid());
        assertEquals("Invoice must have a description.", result.getErrorMessage().get(0));
    }

    @Test
    void testValidate_InvoiceWithoutPositions() {
        InvoiceDto invoiceDto = mock(InvoiceDto.class);
        when(invoiceDto.invoiceDescription()).thenReturn("Invoice description");
        when(invoiceDto.positions()).thenReturn(null);

        ValidationResult result = validator.validate(invoiceDto);

        assertFalse(result.isValid());
        assertEquals("Invoice must have at least one position.", result.getErrorMessage().get(0));
    }

    @Test
    void testValidate_ValidInvoice() {
        InvoiceDto invoiceDto = mock(InvoiceDto.class);
        when(invoiceDto.invoiceDescription()).thenReturn("Invoice description");
        when(invoiceDto.positions()).thenReturn(List.of(mock(InvoicePositionDto.class)));

        ValidationResult result = validator.validate(invoiceDto);

        assertTrue(result.isValid());
    }

    @Test
    void testValidate_InvoicePositionAmountNegative() {
        InvoiceDto invoiceDto = mock(InvoiceDto.class);
        when(invoiceDto.invoiceDescription()).thenReturn("Invoice description");

        InvoicePositionDto position = mock(InvoicePositionDto.class);
        when(position.amount()).thenReturn(new BigDecimal("-10"));
        when(invoiceDto.positions()).thenReturn(List.of(position));

        ValidationResult result = validator.validate(invoiceDto);

        assertFalse(result.isValid());
        assertEquals("Position amount must be positive", result.getErrorMessage().get(0));
    }

    @Test
    void testValidate_MultipleErrors() {
        InvoiceDto invoiceDto = mock(InvoiceDto.class);

        when(invoiceDto.invoiceDescription()).thenReturn(null);

        InvoicePositionDto positionWithNegativeAmount = mock(InvoicePositionDto.class);
        when(positionWithNegativeAmount.amount()).thenReturn(new BigDecimal("-10"));
        when(invoiceDto.positions()).thenReturn(List.of(positionWithNegativeAmount));

        ValidationResult result = validator.validate(invoiceDto);

        assertFalse(result.isValid());
        assertEquals(List.of(
                "Invoice must have a description",
                "Position amount must be positive"
        ), result.getErrorMessage());
    }
}
