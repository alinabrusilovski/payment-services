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
        String relationId = MDC.get("relationId");

        logger.info("Received request to create an invoice: {} with relationId: {}", invoiceDto, relationId);

        ValidationResult<InvoiceEntity> result = service.createInvoice(invoiceDto);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getValue());
        } else {
            logger.error("Error occurred while creating invoice with relationId {}: {}", relationId, result.getError());
            return ResponseEntity.status(400).body(Map.of("error", result.getError()));
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<InvoiceEntity>> getAllInvoices() {
        String relationId = MDC.get("relationId");

        logger.info("Received request to get all invoices with relationId: {}", relationId);

        try {
            ValidationResult<List<InvoiceEntity>> result = service.getAllInvoices();

            if (result.isFailure()) {
                return ResponseEntity.status(500).body(Collections.emptyList());
            }

            List<InvoiceEntity> invoices = result.getValue().orElse(Collections.emptyList());

            if (invoices.isEmpty()) {
                logger.info("No invoices found with relationId: {}", relationId);
                return ResponseEntity.noContent().build();
            } else {
                logger.info("Returning {} invoices with relationId: {}", invoices.size(), relationId);
                return ResponseEntity.ok(invoices);
            }
        } catch (Exception e) {
            logger.error("Error occurred while retrieving invoices with relationId {}: {}", relationId, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
}
