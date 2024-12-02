package com.paymentservice.validation;

import com.paymentservice.dto.PayerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PayerDtoValidatorTest {

    private PayerDto payerDto;
    private PayerDtoValidator validator;

    @BeforeEach
    void setUp() {
        payerDto = mock(PayerDto.class);
        when(payerDto.getPayerId()).thenReturn(1000);
        when(payerDto.getName()).thenReturn("Name");
        when(payerDto.getSecondName()).thenReturn("Secondname");
        when(payerDto.getBirthDate()).thenReturn(LocalDate.of(2000, 1, 1));
        when(payerDto.getEmail()).thenReturn("name@example.com");
        when(payerDto.getPhone()).thenReturn("0501234567");

        validator = new PayerDtoValidator();
    }

    @Test
    void testValidate_SuccessfulValidation() {
        ValidationResult<PayerDto> result = validator.validate(payerDto);

        assertTrue(result.isSuccess());
        assertFalse(result.getError().isPresent());
    }

    @Test
    void testValidate_InvalidId() {
        when(payerDto.getPayerId()).thenReturn(-1);

        ValidationResult<PayerDto> result = validator.validate(payerDto);
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

    @Test
    void testValidate_NameMissing() {
        when(payerDto.getName()).thenReturn(null);

        ValidationResult<PayerDto> result = validator.validate(payerDto);
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

    @Test
    void testValidate_SecondNameMissing() {
        when(payerDto.getSecondName()).thenReturn(null);

        ValidationResult<PayerDto> result = validator.validate(payerDto);
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

    @Test
    void testValidate_InvalidBirthDate() {
        when(payerDto.getBirthDate()).thenReturn(LocalDate.of(1888, 1, 1));

        ValidationResult<PayerDto> result = validator.validate(payerDto);
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

    @Test
    void testValidate_InvalidBirthDateIsInFuture() {
        when(payerDto.getBirthDate()).thenReturn(LocalDate.of(2100, 1, 1));

        ValidationResult<PayerDto> result = validator.validate(payerDto);
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

    @Test
    void testValidate_InvalidPhone() {
        when(payerDto.getPhone()).thenReturn("12345");

        ValidationResult<PayerDto> result = validator.validate(payerDto);
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

    @Test
    void testValidate_InvalidEmail() {
        when(payerDto.getEmail()).thenReturn("invalid_email");

        ValidationResult<PayerDto> result = validator.validate(payerDto);
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

    @Test
    void testValidate_MissingPhoneAndEmail() {
        when(payerDto.getPhone()).thenReturn(null);
        when(payerDto.getEmail()).thenReturn(null);

        ValidationResult<PayerDto> result = validator.validate(payerDto);
        assertFalse(result.isSuccess());
        assertTrue(result.getError().isPresent());
    }

    @Test
    void testValidate_ValidPhoneAndNullEmail() {
        when(payerDto.getEmail()).thenReturn(null);

        ValidationResult<PayerDto> result = validator.validate(payerDto);
        assertTrue(result.isSuccess());
        assertFalse(result.getError().isPresent());
    }

    @Test
    void testValidate_NullPhoneAndValidEmail() {
        when(payerDto.getPhone()).thenReturn(null);

        ValidationResult<PayerDto> result = validator.validate(payerDto);
        assertTrue(result.isSuccess());
        assertFalse(result.getError().isPresent());
    }
}
