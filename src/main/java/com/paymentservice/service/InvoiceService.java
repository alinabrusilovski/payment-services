package com.paymentservice.service;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.dto.PayerDto;
import com.paymentservice.entity.InvoiceEntity;
import com.paymentservice.entity.InvoicePositionEntity;
import com.paymentservice.entity.PayerEntity;
import com.paymentservice.repository.InvoiceRepository;
import com.paymentservice.repository.PayerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService implements IInvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    PayerRepository payerRepository;

//    @Override
//    public InvoiceDto createInvoice(InvoiceDto invoice) {
//        return invoiceRepository.save(invoice.mapToInvoiceEntity());
//    }
//
//    @Override
//    public List<InvoiceDto> getAllInvoices() {
//        return invoiceRepository.findAll();
//    }
//
//    @Override
//    public Optional<InvoiceDto> getInvoiceById(Long id) {
//        return invoiceRepository.findById(id);
//    }
//
//    // Найти плательщика по ID для инвойса
//    @Override
//    public Optional<PayerDto> getPayerById(Long payerId) {
//        return payerRepository.findById(payerId);
//    }
//
//
    public InvoiceDto mapToInvoiceDto(InvoiceEntity invoiceEntity) {
        return new InvoiceDto(
                invoiceEntity.getInvoiceId(),
                invoiceEntity.getSystemId(),
                invoiceEntity.getPayer().getPayerId(),
                invoiceEntity.getInvoiceDescription()
        );
    }
//    public InvoiceEntity mapToInvoiceEntity(InvoiceDto invoiceDto, List<InvoicePositionDto> invoicePositionsDto) {
//        PayerEntity payerEntity = payerRepository.findById(invoiceDto.payerId())
//                .orElseThrow(() -> new EntityNotFoundException("Payer not found with ID: " + invoiceDto.payerId()));
//
//        List<InvoicePositionEntity> invoicePositions = invoicePositionsDto.stream()
//                .map(this::mapToInvoicePositionEntity) // Используем метод преобразования
//                .collect(Collectors.toList());
//
//        return new InvoiceEntity(
//                invoiceDto.invoiceId(),
//                invoiceDto.systemId(),
//                payerEntity,
//                invoiceDto.invoiceDescription(),
//                invoicePositions
//        );
//    }

}
