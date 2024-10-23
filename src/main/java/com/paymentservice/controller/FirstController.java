package com.paymentservice.controller;

import com.paymentservice.service.IExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {
    private final IExperimentService experimentService;

    @Autowired
    public FirstController(IExperimentService experimentService) {
        this.experimentService = experimentService;
    }

    @GetMapping("/first")
    public String getMessage() {
        return experimentService.getMessage();
    }
}