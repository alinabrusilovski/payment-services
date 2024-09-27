//package com.paymentservice.service;
//
//import com.paymentservice.dto.InvoiceDto;
//import com.paymentservice.dto.InvoicePositionDto;
//import com.paymentservice.entity.InvoiceEntity;
//import com.paymentservice.entity.InvoicePositionEntity;
//import com.paymentservice.repository.InvoicePositionRepository;
//import com.paymentservice.repository.InvoiceRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class InvoicePositionService implements IInvoicePositionService{
//    private final InvoicePositionRepository invoicePositionRepository;
//    private final InvoiceRepository invoiceRepository;
//
//    @Autowired
//    public InvoicePositionService(InvoicePositionRepository invoicePositionRepository, InvoiceRepository invoiceRepository) {
//        this.invoicePositionRepository = invoicePositionRepository;
//        this.invoiceRepository = invoiceRepository;
//    }
//
//    // Метод для создания новой позиции инвойса
//    public InvoicePositionDto createInvoicePosition(InvoicePositionDto invoicePosition, Long invoiceId) {
//        Optional<InvoiceDto> invoiceOptional = invoiceRepository.findById(invoiceId);
//        if (invoiceOptional.isPresent()) {
//            invoicePosition.setInvoice(invoiceOptional.get());
//            return invoicePositionRepository.save(invoicePosition);
//        } else {
//            throw new RuntimeException("Инвойс с ID " + invoiceId + " не найден.");
//        }
//    }
//
//    // Метод для получения всех позиций по инвойсу
//    public List<InvoicePositionDto> getInvoicePositionsByInvoiceId(Long invoiceId) {
//        return invoicePositionRepository.findByInvoice_InvoiceId(invoiceId);
//    }
//
//    // Метод для удаления позиции инвойса по ID
//    public void deleteInvoicePosition(Long invoicePositionId) {
//        invoicePositionRepository.deleteById(invoicePositionId);
//    }
//
//    // Метод для получения позиции инвойса по ID
//    public Optional<InvoicePositionDto> getInvoicePositionById(Long invoicePositionId) {
//        return invoicePositionRepository.findById(invoicePositionId);
//    }
//
//    private InvoicePositionEntity mapToInvoicePositionEntity(InvoicePositionDto invoicePositionDto) {
//        return new InvoicePositionEntity(
//                invoicePositionDto.invoicePositionId(), // или null, если это новый объект
//                invoicePositionDto.invoiceId(),
//                invoicePositionDto.invoicePositionDescription(),
//                invoicePositionDto.amount()
//        );
//    }
//}

