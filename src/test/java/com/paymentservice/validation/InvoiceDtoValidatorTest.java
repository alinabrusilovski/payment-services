package com.paymentservice.validation;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.dto.InvoicePositionDto;
import com.paymentservice.dto.PayerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InvoiceDtoValidatorTest {

    private InvoiceDtoValidator validator;
    private PayerDto payerDto;
    private InvoicePositionDto invoicePositionDto;
    private InvoiceDto invoiceDto;

    @BeforeEach
    void setUp() {

        payerDto = mock(PayerDto.class);
        when(payerDto.getPayerId()).thenReturn(1000);
        when(payerDto.getName()).thenReturn("Name");
        when(payerDto.getSecondName()).thenReturn("Secondname");
        when(payerDto.getBirthDate()).thenReturn(LocalDate.of(2000, 1, 1));
        when(payerDto.getEmail()).thenReturn("name@example.com");
        when(payerDto.getPhone()).thenReturn("0501234567");

        invoicePositionDto = mock(InvoicePositionDto.class);
        when(invoicePositionDto.invoicePositionId()).thenReturn(1000);
        when(invoicePositionDto.invoicePositionDescription()).thenReturn("Product description");
        when(invoicePositionDto.amount()).thenReturn(BigDecimal.valueOf(123.45));

        invoiceDto = mock(InvoiceDto.class);
        when(invoiceDto.systemId()).thenReturn(1000);
        when(invoiceDto.invoiceDescription()).thenReturn("Invoice description");
        when(invoiceDto.payer()).thenReturn(payerDto);
        when(invoiceDto.positions()).thenReturn(List.of(invoicePositionDto));

        validator = new InvoiceDtoValidator(new PayerDtoValidator(), new InvoicePositionDtoValidator());
    }

    @Test
    void testValidate_SuccessfulValidation() {
        ValidationResult<InvoiceDto> result = validator.validate(invoiceDto);

        assertTrue(result.isSuccess());
        assertFalse(result.getError().isPresent());
    }

    @Test
    void testValidate_InvoiceMissing() {
        ValidationResult<InvoiceDto> result = validator.validate(null);
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

    @Test
    void testValidate_InvalidId() {
        when(invoiceDto.systemId()).thenReturn(-1000);

        ValidationResult<InvoiceDto> result = validator.validate(invoiceDto);
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

    @Test
    void testValidate_PayerMissing() {
        when(invoiceDto.payer()).thenReturn(null);

        ValidationResult<InvoiceDto> result = validator.validate(invoiceDto);
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

    @Test
    void testValidate_InvoiceWithoutPositions() {
        when(invoiceDto.positions()).thenReturn(null);

        ValidationResult<InvoiceDto> result = validator.validate(invoiceDto);
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

    @Test
    void testValidate_InvoiceDescriptionMissing() {
        when(invoiceDto.invoiceDescription()).thenReturn(null);

        ValidationResult<InvoiceDto> result = validator.validate(invoiceDto);
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

    @Test
    void testValidate_InvoiceDescriptionIsEmpty() {
        when(invoiceDto.invoiceDescription()).thenReturn("");

        ValidationResult<InvoiceDto> result = validator.validate(invoiceDto);
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

    @Test
    void testValidate_PayerValidationFails() {
        when(payerDto.getName()).thenReturn("");

        ValidationResult<InvoiceDto> result = validator.validate(invoiceDto);
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

    @Test
    void testValidate_PositionValidationFails() {
        when(invoicePositionDto.amount()).thenReturn(BigDecimal.valueOf(-1));

        ValidationResult<InvoiceDto> result = validator.validate(invoiceDto);
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

}
