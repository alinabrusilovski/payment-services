package com.paymentservice.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Getter
public class ValidationResult {

    private final boolean valid;
    private final List<String> errorMessage;

    public static ValidationResult success() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult failure(List<String> errors) {
        return new ValidationResult(false, errors);
    }

    public static ValidationResult failure(String error) {
        return new ValidationResult(false, Collections.singletonList(error));
    }
}
