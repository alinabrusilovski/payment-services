package com.paymentservice.controller;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.dto.JsonWrapper;
import com.paymentservice.dto.OperationResult;
import com.paymentservice.entity.InvoiceEntity;
import com.paymentservice.service.IPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/invoices")
@Slf4j
public class InvoiceController {

    @Autowired
    private IPaymentService service;

    @PostMapping()
    public ResponseEntity<Object> createInvoice(@RequestBody InvoiceDto invoiceDto) {

        log.info("Received request to create an invoice: {}", invoiceDto);

        OperationResult<InvoiceEntity> result = service.createInvoice(invoiceDto);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getValue());
        } else {
            log.error("Error occurred while creating invoice: {}", result.getError());
            return ResponseEntity.status(400).body(Map.of("error", result.getError()));
        }
    }

    @GetMapping()
    public ResponseEntity<JsonWrapper<List<InvoiceEntity>>> getAllInvoices(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "sortBy", defaultValue = "invoiceId") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection) {

        if (limit > 1000) {
            limit = 1000;
        }

        log.info("Received request to get invoices with offset={}, limit={}, sortBy={}, sortDirection={}",
                offset, limit, sortBy, sortDirection);

        OperationResult<JsonWrapper<List<InvoiceEntity>>> result = service.getAllInvoices(offset, limit, sortBy, sortDirection);

        if (result.isFailure()) {
            log.warn("Service returned failure: {}", result.getError());
            return ResponseEntity.status(500).body(new JsonWrapper<>(Collections.emptyList()));
        }

        JsonWrapper<List<InvoiceEntity>> wrapper = result.getValue().orElse(new JsonWrapper<>(Collections.emptyList()));

        if (wrapper.getValue().isEmpty()) {
            log.info("No invoices found");
            return ResponseEntity.status(500).body(new JsonWrapper<>(Collections.emptyList()));
        } else {
            log.info("Returning {} invoices", wrapper.getValue().size());
            return ResponseEntity.ok(wrapper);
        }
    }

}
