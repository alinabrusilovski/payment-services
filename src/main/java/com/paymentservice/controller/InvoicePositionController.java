//package com.paymentservice.controller;
//
//import com.paymentservice.dto.InvoicePositionDto;
//import com.paymentservice.service.IPaymentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//
//@RestController
//@RequestMapping("/positions")
//public class InvoicePositionController {
//
//    @Autowired
//    private IPaymentService service;

//    @PostMapping("/create/{invoiceId}")
//    public InvoicePositionDto createInvoicePosition(@RequestBody InvoicePositionDto invoicePositionDto, @PathVariable Integer invoiceId) {
//        return service.createInvoicePosition(invoicePositionDto, invoiceId);
//    }
//
//    @GetMapping("/get/byinvoiceid/{invoiceId}")
//    public List<InvoicePositionDto> getInvoicePositionsByInvoiceId(@PathVariable Integer invoiceId) {
//        return service.getInvoicePositionsByInvoiceId(invoiceId);
//    }
//
//    @GetMapping("/get/bypositionid/{positionId}")
//    public InvoicePositionDto getInvoicePositionById(@PathVariable Integer positionId) {
//        return service.getInvoicePositionById(positionId);
//    }
//
//    @PostMapping("/update/{positionId}")
//    public InvoicePositionDto updateInvoicePosition(@PathVariable Integer positionId, @RequestBody InvoicePositionDto positionDto) {
//        return service.updateInvoicePosition(positionId, positionDto);
//    }
//
//    @DeleteMapping("/delete/{positionId}")
//    public void deleteInvoicePosition(Integer positionId) {
//        service.deleteInvoicePosition(positionId);
//    }
//}
