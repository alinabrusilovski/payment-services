package com.paymentservice.validation;

import com.paymentservice.dto.PayerDto;
import com.paymentservice.entity.PayerEntity;
import com.paymentservice.repository.PayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        when(payerDto.name()).thenReturn(null);

        ValidationResult result = validator.validate(payerDto);

        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("Payer must have a name"));
    }

    @Test
    void testValidate_EmptyPhone() {
        PayerDto payerDto = mock(PayerDto.class);
        when(payerDto.phone()).thenReturn("");

        ValidationResult result = validator.validate(payerDto);

        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("Phone number must be provided"));
    }

    @Test
    void testValidate_InvalidPhone() {
        PayerDto payerDto = mock(PayerDto.class);
        when(payerDto.phone()).thenReturn("12345");

        ValidationResult result = validator.validate(payerDto);

        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("Phone number is invalid"));
    }

    @Test
    void testValidate_InvalidEmail() {
        PayerDto payerDto = mock(PayerDto.class);
        when(payerDto.email()).thenReturn("invalid_email");

        ValidationResult result = validator.validate(payerDto);

        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("Email is invalid"));
    }

    @Test
    void testValidate_PayerDoesNotExistInDatabase() {
        PayerDto payerDto = mock(PayerDto.class);
        when(payerDto.payerId()).thenReturn(999);
        when(payerRepository.findById(999)).thenReturn(Optional.empty());

        ValidationResult result = validator.validate(payerDto);

        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("Payer does not exist in the database"));
    }

    @Test
    void testValidate_MultipleErrors() {
        PayerDto payerDto = mock(PayerDto.class);

        when(payerDto.name()).thenReturn(null);
        when(payerDto.phone()).thenReturn("12345");
        when(payerDto.email()).thenReturn("invalid_email");
        when(payerDto.payerId()).thenReturn(999);
        when(payerRepository.findById(999)).thenReturn(Optional.empty());

        ValidationResult result = validator.validate(payerDto);

        assertFalse(result.isValid());
        assertEquals(
                List.of(
                        "Payer must have a name",
                        "Phone number is invalid",
                        "Email is invalid",
                        "Payer does not exist in the database"
                ),
                result.getErrorMessage()
        );
    }

    @Test
    void testValidate_ValidPayer() {
        PayerDto payerDto = mock(PayerDto.class);
        when(payerDto.name()).thenReturn("Name Secondname");
        when(payerDto.phone()).thenReturn("0559115555");
        when(payerDto.email()).thenReturn("name@example.com");
        when(payerDto.payerId()).thenReturn(100);

        PayerEntity payerEntity = mock(PayerEntity.class);
        when(payerRepository.findById(100)).thenReturn(Optional.of(payerEntity));

        ValidationResult result = validator.validate(payerDto);

        assertTrue(result.isValid());
        assertNull(result.getErrorMessage());
    }
}
