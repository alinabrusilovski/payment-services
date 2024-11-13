package com.paymentservice.validation;

import java.util.Collections;
import java.util.List;


public record ValidationResult(boolean valid, List<String> errorMessage) {

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
