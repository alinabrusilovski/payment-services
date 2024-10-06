package com.paymentservice.dto;

import java.time.LocalDate;

public record PayerDto(Integer payerId, String name, String secondName, LocalDate birthDate, String email, String phone) {

}

