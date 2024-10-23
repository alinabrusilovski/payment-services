package com.paymentservice.service;

import org.springframework.stereotype.Component;

@Component
public class SecondService implements IExperimentService {
    @Override
    public String getMessage() {
        return "Hello from second service";
    }
}
