package com.paymentservice.controller;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.entity.InvoiceEntity;
import com.paymentservice.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    private IPaymentService service;

    @PostMapping("/create")
    public ResponseEntity<InvoiceEntity> createInvoice(@RequestBody InvoiceDto invoiceDto) {
        logger.info("Received request to create an invoice: {}", invoiceDto);

        try {
            InvoiceEntity createdInvoice = service.createInvoice(invoiceDto);
            logger.info("Invoice created successfully: {}", createdInvoice);
            return ResponseEntity.ok(createdInvoice);
        } catch (Exception e) {
            logger.error("Error occurred while creating invoice: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<InvoiceEntity>> getAllInvoices(){
        logger.info("Received request to get all invoices.");

        try {
            List<InvoiceEntity> invoices = service.getAllInvoices();

            if (invoices.isEmpty()) {
                logger.info("No invoices found.");
                return ResponseEntity.noContent().build(); // Возвращаем HTTP 204, если инвойсов нет
            } else {
                logger.info("Returning {} invoices.", invoices.size());
                return ResponseEntity.ok(invoices);
            }

        } catch (Exception e) {
            logger.error("Error occurred while retrieving invoices: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build(); // Возвращаем HTTP 500 в случае ошибки
        }
    }
}