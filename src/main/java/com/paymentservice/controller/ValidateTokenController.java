package com.paymentservice.controller;

import com.paymentservice.dto.ErrorResponseDto;
import com.paymentservice.dto.JsonWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValidateTokenController {
    @GetMapping("/validate")
    public ResponseEntity<JsonWrapper<Object>> validateToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            ErrorResponseDto errorResponse = new ErrorResponseDto("Unauthorized", "Token is not valid or missing");
            return ResponseEntity.status(401).body(new JsonWrapper<>(null, errorResponse));
        }

        if (authentication.getAuthorities().stream().noneMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            ErrorResponseDto errorResponse = new ErrorResponseDto("Forbidden", "User is not an admin");
            return ResponseEntity.status(403).body(new JsonWrapper<>(null, errorResponse));
        }

        return ResponseEntity.ok(new JsonWrapper<>(new Object(), null));
    }
}
