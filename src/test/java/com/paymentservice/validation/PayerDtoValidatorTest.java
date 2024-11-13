package com.paymentservice.validation;

import com.paymentservice.dto.PayerDto;
import com.paymentservice.entity.PayerEntity;
import com.paymentservice.repository.PayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PayerDtoValidatorTest {

    private PayerRepository payerRepository;
    private PayerDtoValidator validator;

    @BeforeEach
    void setUp() {
        payerRepository = mock(PayerRepository.class);

        validator = new PayerDtoValidator();
        validator.payerRepository = payerRepository;
    }

    @Test
    void testValidate_NameMissing() {
        PayerDto payerDto = mock(PayerDto.class);
        when(payerDto.getName()).thenReturn(null);

        ValidationResult result = validator.validate(payerDto);

        assertFalse(result.valid());
        assertTrue(result.errorMessage().contains("Payer must have a name"));
    }

    @Test
    void testValidate_SecondNameMissing() {
        PayerDto payerDto = mock(PayerDto.class);
        when(payerDto.getSecondName()).thenReturn(null);

        ValidationResult result = validator.validate(payerDto);

        assertFalse(result.valid());
        assertTrue(result.errorMessage().contains("Payer must have a second name"));
    }

    @Test
    void testValidate_InvalidPhone() {
        PayerDto payerDto = mock(PayerDto.class);
        when(payerDto.getPhone()).thenReturn("12345");

        ValidationResult result = validator.validate(payerDto);

        assertFalse(result.valid());
        assertTrue(result.errorMessage().contains("Phone number is invalid"));
    }

    @Test
    void testValidate_PhoneFormatting() {
        PayerDto payerDto = spy(new PayerDto(null, "Name", "Secondname", null, null, "0501234567"));

        when(payerRepository.findById(anyInt())).thenReturn(Optional.of(new PayerEntity()));

        ValidationResult result = validator.validate(payerDto);

        verify(payerDto).setPhone("+972 50-1234567");
        assertTrue(result.valid());
        assertNull(result.errorMessage());
    }

    @Test
    void testValidate_InvalidEmail() {
        PayerDto payerDto = mock(PayerDto.class);
        when(payerDto.getEmail()).thenReturn("invalid_email");

        ValidationResult result = validator.validate(payerDto);

        assertFalse(result.valid());
        assertTrue(result.errorMessage().contains("Email is invalid"));
    }

    @Test
    void testValidate_EmailFormatting() {
        PayerDto payerDto = spy(new PayerDto(null, "Name", "Secondname", null, "NameSecondName@Example.Com", null));

        ValidationResult result = validator.validate(payerDto);

        assertEquals("namesecondname@example.com", payerDto.getEmail());
        assertTrue(result.valid());
        verify(payerDto).setEmail("namesecondname@example.com");

    }

    @Test
    void testValidate_PayerDoesNotExistInDatabase() {
        PayerDto payerDto = mock(PayerDto.class);
        when(payerDto.getPayerId()).thenReturn(999);
        when(payerRepository.findById(999)).thenReturn(Optional.empty());

        ValidationResult result = validator.validate(payerDto);

        assertFalse(result.valid());
        assertTrue(result.errorMessage().contains("Payer does not exist in the database"));
    }

    @Test
    void testValidate_MultipleErrors() {
        PayerDto payerDto = mock(PayerDto.class);

        when(payerDto.getName()).thenReturn(null);
        when(payerDto.getSecondName()).thenReturn(null);
        when(payerDto.getPhone()).thenReturn("12345");
        when(payerDto.getEmail()).thenReturn("invalid_email");
        when(payerDto.getPayerId()).thenReturn(999);
        when(payerRepository.findById(999)).thenReturn(Optional.empty());

        ValidationResult result = validator.validate(payerDto);

        assertFalse(result.valid());
        assertEquals(
                List.of(
                        "Payer must have a name",
                        "Payer must have a second name",
                        "Phone number is invalid",
                        "Email is invalid",
                        "Payer does not exist in the database"
                ),
                result.errorMessage()
        );
    }

    @Test
    void testValidate_MissingPhoneAndEmail() {
        PayerDto payerDto = mock(PayerDto.class);
        when(payerDto.getPhone()).thenReturn(null);
        when(payerDto.getEmail()).thenReturn(null);

        ValidationResult result = validator.validate(payerDto);

        assertFalse(result.valid());
        assertTrue(result.errorMessage().contains("Either email or phone must be provided"));
    }

    @Test
    void testValidate_ValidPhoneAndEmail() {
        PayerDto payerDto = spy(new PayerDto(null, "Name", "Secondname", null, "valid.email@example.com", "0508370571"));

        ValidationResult result = validator.validate(payerDto);

        assertTrue(result.valid());
        assertNull(result.errorMessage());
    }

    @Test
    void testValidate_ValidPhoneAndNullEmail() {
        PayerDto payerDto = spy(new PayerDto(null, "Name", "Secondname", null, null, "0521234567"));

        ValidationResult result = validator.validate(payerDto);

        assertTrue(result.valid());
        assertNull(result.errorMessage());
    }

    @Test
    void testValidate_NullPhoneAndValidEmail() {
        PayerDto payerDto = spy(new PayerDto(null, "Name", "SecondName", null, "valid.email@example.com", null));

        ValidationResult result = validator.validate(payerDto);

        assertTrue(result.valid());
        assertNull(result.errorMessage());
    }


    @Test
    void testValidate_ValidPayer() {
        PayerDto payerDto = mock(PayerDto.class);
        when(payerDto.getName()).thenReturn("Name");
        when(payerDto.getSecondName()).thenReturn("Secondname");
        when(payerDto.getPhone()).thenReturn("0559115555");
        when(payerDto.getEmail()).thenReturn("name@example.co.il");
        when(payerDto.getPayerId()).thenReturn(100);

        PayerEntity payerEntity = mock(PayerEntity.class);
        when(payerRepository.findById(100)).thenReturn(Optional.of(payerEntity));

        ValidationResult result = validator.validate(payerDto);

        assertTrue(result.valid());
        assertNull(result.errorMessage());
    }
}
