package com.paymentservice.controller;

import com.paymentservice.service.IExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecondController {

    private final IExperimentService experimentService;

    @Autowired
    public SecondController(@Qualifier("secondService") IExperimentService experimentService) {
        this.experimentService = experimentService;
    }

    @GetMapping("/second")
    public String getMessage() {
        return experimentService.getMessage();
    }
}
