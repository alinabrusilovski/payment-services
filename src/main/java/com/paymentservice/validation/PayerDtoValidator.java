package com.paymentservice.validation;

import com.paymentservice.dto.PayerDto;
import com.paymentservice.repository.PayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Component
public class PayerDtoValidator implements IValidator<PayerDto> {

    @Autowired
    public PayerRepository payerRepository;

    private static final String PHONE_PATTERN = "^[0][5-9][0-9]\\d{7}$";
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    @Override
    public ValidationResult<PayerDto> validate(PayerDto payerDto) {

        if (payerDto.getPayerId() != null && payerDto.getPayerId() <= 0)
            return ValidationResult.failure("Payer id must be positive");

        if (payerDto.getName() == null || payerDto.getName().isEmpty())
            return ValidationResult.failure("Payer must have a name");

        if (payerDto.getSecondName() == null || payerDto.getSecondName().isEmpty())
            return ValidationResult.failure("Payer must have a second name");

        LocalDate minDate = LocalDate.of(1900, 1, 1);
        LocalDate maxDate = LocalDate.now().plusDays(1);
        if (payerDto.getBirthDate().isBefore(minDate) || payerDto.getBirthDate().isAfter(maxDate))
            return ValidationResult.failure("Invalid birth date");

        if ((payerDto.getPhone() == null || payerDto.getPhone().isEmpty()) &&
                (payerDto.getEmail() == null || payerDto.getEmail().isEmpty()))
            return ValidationResult.failure("Either email or phone must be provided");

        if (payerDto.getPhone() != null && !payerDto.getPhone().isEmpty()) {
            if (!isValidPhone(payerDto.getPhone()))
                return ValidationResult.failure("Phone number is invalid");
            else
                payerDto.setPhone(formatPhone(payerDto.getPhone()));
        }

        if (payerDto.getEmail() != null && !payerDto.getEmail().isEmpty()) {
            if (!isValidEmail(payerDto.getEmail()))
                return ValidationResult.failure("Email is invalid");
            else
                payerDto.setEmail(formatEmail(payerDto.getEmail()));
        }

        return ValidationResult.success(payerDto);
    }

    private boolean isValidPhone(String phone) {
        return Pattern.matches(PHONE_PATTERN, phone);
    }

    private String formatPhone(String phone) {
        return "+972 " + phone.substring(1, 3) + "-" + phone.substring(3);
    }

    private boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_PATTERN, email);
    }

    private String formatEmail(String email) {
        return email.toLowerCase();
    }
}
