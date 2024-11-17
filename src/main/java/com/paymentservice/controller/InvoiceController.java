package com.paymentservice.controller;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.entity.InvoiceEntity;
import com.paymentservice.service.IPaymentService;
import com.paymentservice.validation.ValidationResult;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    private IPaymentService service;

    @PostMapping("/create")
    public ResponseEntity<Object> createInvoice(@RequestBody InvoiceDto invoiceDto) {
        String relationId = UUID.randomUUID().toString();
        MDC.put("relationId", relationId);

        logger.info("Received request to create an invoice: {}", invoiceDto);

        ValidationResult<InvoiceEntity> result = service.createInvoice(invoiceDto);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getValue());
        } else {
            logger.error("Error occurred while creating invoice: {}", result.getError());

            return ResponseEntity.status(400).body(Map.of("error", result.getError()));
        }
    }


    @GetMapping("/get/all")
    public ResponseEntity<List<InvoiceEntity>> getAllInvoices() {
        String relationId = UUID.randomUUID().toString();
        MDC.put("relationId", relationId);

        logger.info("Received request to get all invoices.");

        try {
            ValidationResult<List<InvoiceEntity>> result = service.getAllInvoices();

            if (result.isFailure()) {
                return ResponseEntity.status(500).body(Collections.emptyList());
            }

            List<InvoiceEntity> invoices = result.getValue().orElse(Collections.emptyList());

            if (invoices.isEmpty()) {
                logger.info("No invoices found.");
                return ResponseEntity.noContent().build();
            } else {
                logger.info("Returning {} invoices.", invoices.size());
                return ResponseEntity.ok(invoices);
            }

        } catch (Exception e) {
            logger.error("Error occurred while retrieving invoices: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        } finally {
            MDC.clear();
        }
    }
}