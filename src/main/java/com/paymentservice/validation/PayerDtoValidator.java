package com.paymentservice.validation;

import com.paymentservice.dto.PayerDto;
import com.paymentservice.repository.PayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class PayerDtoValidator implements IValidator<PayerDto> {

    @Autowired
    private PayerRepository payerRepository;

    private static final String PHONE_PATTERN = "^0[5-9]\\d{8}$";
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.(gov|co|com|org|net|il)$";

    @Override
    public ValidationResult validate(PayerDto payerDto) {
        List<String> errors = new ArrayList<>();

        if (payerDto.name() == null || payerDto.name().isEmpty()) {
            errors.add("Payer must have a name");
        }

        if (payerDto.phone() == null || payerDto.phone().isEmpty()) {
            errors.add("Phone number must be provided");
        } else if (!isValidPhone(payerDto.phone())) {
            errors.add("Phone number is invalid");
        }

        if (payerDto.email() != null && !isValidEmail(payerDto.email())) {
            errors.add("Email is invalid");
        }

        if (payerDto.payerId() != null && payerRepository.findById(payerDto.payerId()).isEmpty()) {
            errors.add("Payer does not exist in the database");
        }

        return errors.isEmpty() ? ValidationResult.success() : ValidationResult.failure(errors);
    }

    private boolean isValidPhone(String phone) {
        phone = formatPhone(phone);
        return Pattern.matches(PHONE_PATTERN, phone);
    }

    private String formatPhone(String phone) {
        return "+972 " + phone.substring(1, 3) + "-" + phone.substring(3);
    }

    private boolean isValidEmail(String email) {
        return email != null && Pattern.matches(EMAIL_PATTERN, email);
    }
}
