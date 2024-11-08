package com.paymentservice.validation;

import com.paymentservice.dto.PayerDto;
import com.paymentservice.repository.PayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PayerDtoValidator {

    @Autowired
    private PayerRepository payerRepository;

    private static final String PHONE_PATTERN = "^0[5-9]\\d{8}$";
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.(gov|co|com|org|net|il)$"; // Регулярное выражение для email (классические и израильские)

    public void validate(PayerDto payerDto) {
        // Проверка на пустое поле имени
        if (payerDto.name() == null || payerDto.name().isEmpty()) {
            throw new IllegalArgumentException("Payer must have a name");
        }

        // Проверка на пустое поле телефона
        if (payerDto.phone() == null || payerDto.phone().isEmpty()) {
            throw new IllegalArgumentException("Phone number must be provided");
        }

        // Проверка на корректность телефона
        if (!isValidPhone(payerDto.phone())) {
            throw new IllegalArgumentException("Phone number is invalid");
        }

        // Проверка на корректность email
        if (payerDto.email() != null && !isValidEmail(payerDto.email())) {
            throw new IllegalArgumentException("Email is invalid");
        }

        // Проверка на существование пейера в базе данных
        if (payerDto.payerId() != null && payerRepository.findById(payerDto.payerId()).isEmpty()) {
            throw new IllegalArgumentException("Payer does not exist in the database");
        }
    }

    private boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }

        // Преобразуем телефон в формат +972 55-9116483
        phone = formatPhone(phone);

        return Pattern.matches(PHONE_PATTERN, phone);
    }

    private String formatPhone(String phone) {
        if (phone == null) {
            return null;
        }

        // Преобразуем телефон в нужный формат
        phone = "+972 " + phone.substring(1, 3) + "-" + phone.substring(3);
        return phone;
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        // Проверка на корректность email
        return Pattern.matches(EMAIL_PATTERN, email);
    }
}
