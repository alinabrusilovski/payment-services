package com.paymentservice.dto;

import java.time.LocalDate;

public record PayerDto(Long payerId, String name, String secondName, LocalDate birthDate, String email, String phone) {

}

