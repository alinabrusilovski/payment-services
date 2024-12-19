package com.paymentservice.controller;

import com.paymentservice.dto.ErrorResponseDto;
import com.paymentservice.dto.JsonWrapper;
import com.paymentservice.dto.OperationResult;
import com.paymentservice.dto.PaymentDto;
import com.paymentservice.entity.PaymentEntity;
import com.paymentservice.service.IPaymentService;
import com.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final IPaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping()
    public ResponseEntity<Object> createPayment(@RequestBody PaymentDto paymentDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            ErrorResponseDto errorResponse = new ErrorResponseDto("Unauthorized", "Authentication required");
            return ResponseEntity.status(401).body(new JsonWrapper<>(null, errorResponse));
        }
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SELLER"))) {
            ErrorResponseDto errorResponse = new ErrorResponseDto("Forbidden", "Only SELLERs can create payments");
            return ResponseEntity.status(403).body(new JsonWrapper<>(null, errorResponse));
        }

        OperationResult<PaymentEntity> result = paymentService.createPayment(
                paymentDto.getInvoicePositionIds(),
                paymentDto.getAmount()
        );

        if (result.isFailure()) {
            ErrorResponseDto errorResponse = new ErrorResponseDto("Failure", result.getError().orElse("Unknown error"));
            return ResponseEntity.status(400).body(new JsonWrapper<>(null, errorResponse));
        }

        return ResponseEntity.ok(new JsonWrapper<>(result.getValue().orElse(null)));
    }
}