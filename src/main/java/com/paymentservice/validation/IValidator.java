package com.paymentservice.validation;

import org.springframework.stereotype.Component;


@Component
public interface IValidator<T> {
    ValidationResult<T> validate(T object);
}
