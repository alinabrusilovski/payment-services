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
    public PayerRepository payerRepository;

    private static final String PHONE_PATTERN = "^[0][5-9][0-9]\\d{7}$";
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    @Override
    public ValidationResult validate(PayerDto payerDto) {
        List<String> errors = new ArrayList<>();

        if (payerDto.getName() == null || payerDto.getName().isEmpty()) {
            errors.add("Payer must have a name");
        }

        if (payerDto.getSecondName() == null || payerDto.getSecondName().isEmpty()) {
            errors.add("Payer must have a second name");
        }

        // Проверяем, чтобы хотя бы один из этих полей был заполнен
        if ((payerDto.getPhone() == null || payerDto.getPhone().isEmpty()) &&
                (payerDto.getEmail() == null || payerDto.getEmail().isEmpty())) {
            errors.add("Either email or phone must be provided");
        }

        // Валидируем phone
        if (payerDto.getPhone() != null && !payerDto.getPhone().isEmpty()) {
            if (!isValidPhone(payerDto.getPhone())) {
                errors.add("Phone number is invalid");
            } else {
                payerDto.setPhone(formatPhone(payerDto.getPhone()));
            }
        }

        // Валидируем email
        if (payerDto.getEmail() != null && !payerDto.getEmail().isEmpty()) {
            if (!isValidEmail(payerDto.getEmail())) {
                errors.add("Email is invalid");
            } else {
                payerDto.setEmail(formatEmail(payerDto.getEmail()));
            }
        }

        if (payerDto.getPayerId() != null && payerRepository.findById(payerDto.getPayerId()).isEmpty()) {
            errors.add("Payer does not exist in the database");
        }

        return errors.isEmpty() ? ValidationResult.success() : ValidationResult.failure(errors);
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
