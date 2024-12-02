package com.paymentservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PayerDto {
    @Nullable
    private Integer payerId;
    private String name;
    private String secondName;
    private LocalDate birthDate;
    @Nullable
    private String email;
    @Nullable
    private String phone;
}


