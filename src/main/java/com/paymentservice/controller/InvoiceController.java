package com.paymentservice.controller;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.entity.InvoiceEntity;
import com.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private PaymentService service;

    @PostMapping("/create")
    public InvoiceEntity createInvoice(@RequestBody InvoiceDto invoiceDto) {
        return service.createInvoice(invoiceDto);
    }

    @GetMapping("/get/all")
    public List<InvoiceEntity> getAllInvoices(){
        return service.getAllInvoices();
    }

}
