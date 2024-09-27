package com.paymentservice.controller;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.dto.PayerDto;
import com.paymentservice.service.PayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
    @RequestMapping("/payers")
    public class PayerController {

    @Autowired
    private PayerService payerService;



    @PostMapping("/create")
    public PayerDto createPayer(@RequestBody PayerDto payerDto){
        return payerService.createPayer(payerDto);
    }

    @GetMapping("/get/{payerId}")
    public Optional<PayerDto> getPayerById(@PathVariable Long payerId){
        return payerService.getPayerById(payerId);
    }
    @GetMapping("/get/all")
    public List<PayerDto> getAllPayers(){
        return payerService.getAllPayers();
    }

    @PostMapping("/update/{payerId}")
    public PayerDto updatePayer(@PathVariable Long payerId, @RequestBody PayerDto updatedPayer){
        return payerService.updatePayer(payerId, updatedPayer);
    }

    @GetMapping("/get/invoices/{payerId}")
    public List<InvoiceDto> getInvoicesByPayerId(@PathVariable Long payerId){
        return payerService.getInvoicesByPayerId(payerId);
    }
}
