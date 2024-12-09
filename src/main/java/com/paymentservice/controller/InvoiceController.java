package com.paymentservice.controller;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.dto.JsonWrapper;
import com.paymentservice.entity.InvoiceEntity;
import com.paymentservice.service.IPaymentService;
import com.paymentservice.validation.ValidationResult;
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

    @PostMapping()
    public ResponseEntity<Object> createInvoice(@RequestBody InvoiceDto invoiceDto) {

        logger.info("Received request to create an invoice: {}", invoiceDto);

        ValidationResult<InvoiceEntity> result = service.createInvoice(invoiceDto);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getValue());
        } else {
            logger.error("Error occurred while creating invoice: {}", result.getError());
            return ResponseEntity.status(400).body(Map.of("error", result.getError()));
        }
    }

    @GetMapping()
    public ResponseEntity<JsonWrapper<List<InvoiceEntity>>> getAllInvoices(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "sortBy", defaultValue = "invoiceId") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection) {

        logger.info("Received request to get invoices with offset={}, limit={}, sortBy={}, sortDirection={}",
                offset, limit, sortBy, sortDirection);

        try {
            ValidationResult<JsonWrapper<List<InvoiceEntity>>> result = service.getAllInvoices(offset, limit, sortBy, sortDirection);

            if (result.isFailure()) {
                logger.warn("Service returned failure: {}", result.getError());
                return ResponseEntity.status(500).body(new JsonWrapper<>(Collections.emptyList()));
            }

            JsonWrapper<List<InvoiceEntity>> wrapper = result.getValue().orElse(new JsonWrapper<>(Collections.emptyList()));

            if (wrapper.getValue().isEmpty()) {
                logger.info("No invoices found");
                return ResponseEntity.noContent().build();
            } else {
                logger.info("Returning {} invoices", wrapper.getValue().size());
                return ResponseEntity.ok(wrapper);
            }
        } catch (Exception e) {
            logger.error("Error occurred while retrieving invoices: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

}
