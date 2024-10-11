//package com.paymentservice.controller;
//
//import com.paymentservice.dto.InvoiceDto;
//import com.paymentservice.dto.PayerDto;
//import com.paymentservice.service.IPaymentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/payers")
//public class PayerController {
//
//    @Autowired
//    private IPaymentService service;
//
//    @PostMapping("/create")
//    public PayerDto createPayer(@RequestBody PayerDto payerDto){
//        return service.createPayer(payerDto);
//    }
//
//    @GetMapping("/get/{payerId}")
//    public Optional<PayerDto> getPayerById(@PathVariable Integer payerId){
//        return service.getPayerById(payerId);
//    }
//    @GetMapping("/get/all")
//    public List<PayerDto> getAllPayers(){
//        return service.getAllPayers();
//    }
//
//    @PostMapping("/update/{payerId}")
//    public PayerDto updatePayer(@PathVariable Integer payerId, @RequestBody PayerDto updatedPayer){
//        return service.updatePayer(payerId, updatedPayer);
//    }
//
//    @GetMapping("/get/invoices/{payerId}")
//    public List<InvoiceDto> getInvoicesByPayerId(@PathVariable Integer payerId){
//        return service.getInvoicesByPayerId(payerId);
//    }
//}
