package com.paymentservice.service;


import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary  // Указывает, что это реализация по умолчанию
@Component
public class FirstService implements IExperimentService {
    @Override
    public String getMessage() {
        return "Hello from first service";
    }
}
