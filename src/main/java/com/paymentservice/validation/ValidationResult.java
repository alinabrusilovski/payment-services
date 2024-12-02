package com.paymentservice.validation;

import java.util.Optional;
import java.util.function.Function;


public class ValidationResult<T> {

    private final T value;
    private final String error;

    private ValidationResult(T value, String error) {
        this.value = value;
        this.error = error;
    }

    public static <T> ValidationResult<T> success(T value) {
        return new ValidationResult<>(value, null);
    }

    public static <T> ValidationResult<T> failure(String error) {
        return error == null || error.isEmpty()
                ? new ValidationResult<>(null, "Error cannot be null or empty")
                : new ValidationResult<>(null, error);
    }

    public boolean isSuccess() {
        return error == null;
    }

    public boolean isFailure() {
        return !isSuccess();
    }

    public Optional<T> getValue() {
        return Optional.ofNullable(value);
    }

    public Optional<String> getError() {
        return Optional.ofNullable(error);
    }

    public <U> ValidationResult<U> map(Function<T, U> mapper) {
        if (isSuccess()) {
            return ValidationResult.success(mapper.apply(value));
        }
        return ValidationResult.failure(error);
    }

    public <U> ValidationResult<U> flatMap(Function<T, ValidationResult<U>> mapper) {
        if (isSuccess()) {
            return mapper.apply(value);
        }
        return ValidationResult.failure(error);
    }
}

