package com.paymentservice.dto;

import org.springframework.lang.Nullable;

import java.time.LocalDate;

public record PayerDto(@Nullable Integer payerId, String name, String secondName, LocalDate birthDate,
                       @Nullable String email, @Nullable String phone) {

    public PayerDto {
        if (email == null && phone == null) {
            throw new IllegalArgumentException("Either email or phone must be provided.");
        }
    }

}

