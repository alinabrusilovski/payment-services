package com.paymentservice.validation;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.dto.InvoicePositionDto;
import com.paymentservice.dto.PayerDto;
import com.paymentservice.repository.PayerRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InvoiceDtoValidatorTest {

    private final InvoiceDtoValidator validator = new InvoiceDtoValidator();

    @Test
    void testValidate_PayerMissing() {
        InvoiceDto invoiceDto = mock(InvoiceDto.class);
        when(invoiceDto.payer()).thenReturn(null);

        ValidationResult result = validator.validate(invoiceDto);

        assertFalse(result.valid());
        assertTrue(result.errorMessage().contains("Invoice must have a payer"));
    }

    @Test
    void testValidate_InvoiceDescriptionMissing() {
        InvoiceDto invoiceDto = mock(InvoiceDto.class);
        PayerDto payerDto = mock(PayerDto.class);
        when(invoiceDto.payer()).thenReturn(payerDto);

        when(invoiceDto.invoiceDescription()).thenReturn(null);

        ValidationResult result = validator.validate(invoiceDto);

        assertFalse(result.valid());
        assertEquals("Invoice must have a description", result.errorMessage().get(0));
    }

    @Test
    void testValidate_InvoiceWithoutPositions() {
        InvoiceDto invoiceDto = mock(InvoiceDto.class);
        PayerDto payerDto = mock(PayerDto.class);
        when(invoiceDto.payer()).thenReturn(payerDto);

        when(invoiceDto.invoiceDescription()).thenReturn("Invoice description");
        when(invoiceDto.positions()).thenReturn(null);

        ValidationResult result = validator.validate(invoiceDto);

        assertFalse(result.valid());
        assertEquals("Invoice must have at least one position", result.errorMessage().get(0));
    }

    @Test
    void testValidate_ValidInvoice() {
        InvoiceDto invoiceDto = mock(InvoiceDto.class);
        PayerDto payerDto = mock(PayerDto.class);
        when(invoiceDto.payer()).thenReturn(payerDto);

        when(invoiceDto.invoiceDescription()).thenReturn("Invoice description");
        InvoicePositionDto positionDto = mock(InvoicePositionDto.class);
        when(positionDto.amount()).thenReturn(new BigDecimal("100.00"));  // Задаем реальную сумму для позиции
        when(invoiceDto.positions()).thenReturn(List.of(positionDto));

        ValidationResult result = validator.validate(invoiceDto);

        assertTrue(result.valid());
    }

    @Test
    void testValidate_InvoicePositionAmountNegative() {
        InvoiceDto invoiceDto = mock(InvoiceDto.class);
        PayerDto payerDto = mock(PayerDto.class);
        when(invoiceDto.payer()).thenReturn(payerDto);
        when(invoiceDto.invoiceDescription()).thenReturn("Invoice description");

        InvoicePositionDto position = mock(InvoicePositionDto.class);
        when(position.amount()).thenReturn(new BigDecimal("-10"));
        when(invoiceDto.positions()).thenReturn(List.of(position));

        ValidationResult result = validator.validate(invoiceDto);

        assertFalse(result.valid());
        assertEquals("Position amount must be positive", result.errorMessage().get(0));
    }

    @Test
    void testValidate_MultipleErrors() {
        InvoiceDto invoiceDto = mock(InvoiceDto.class);
        PayerDto payerDto = mock(PayerDto.class);
        when(invoiceDto.payer()).thenReturn(payerDto);

        when(invoiceDto.invoiceDescription()).thenReturn(null);

        InvoicePositionDto positionWithNegativeAmount = mock(InvoicePositionDto.class);
        when(positionWithNegativeAmount.amount()).thenReturn(new BigDecimal("-10"));
        when(invoiceDto.positions()).thenReturn(List.of(positionWithNegativeAmount));

        ValidationResult result = validator.validate(invoiceDto);

        assertFalse(result.valid());
        assertEquals(List.of(
                "Invoice must have a description",
                "Position amount must be positive"
        ), result.errorMessage());
    }
}
