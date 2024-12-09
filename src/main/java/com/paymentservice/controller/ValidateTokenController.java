package com.paymentservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ValidateTokenController {
    @GetMapping("/validate")
    public ResponseEntity<Map<String, String>> validateToken() {
        return ResponseEntity.ok(Map.of("message", "Token is valid, user is admin"));
    }
}
