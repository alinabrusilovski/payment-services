package com.paymentservice.service;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.dto.PayerDto;
import com.paymentservice.entity.InvoiceEntity;
import com.paymentservice.entity.PayerEntity;
import com.paymentservice.repository.InvoiceRepository;
import com.paymentservice.repository.PayerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PayerService implements IPayerService {

    @Autowired
    PayerRepository payerRepository;
    @Autowired
    InvoiceRepository invoiceEntity;

    @Override
    public PayerDto createPayer(PayerDto payerDto) {
        PayerEntity payerEntity = mapToPayerEntity(payerDto);
        PayerEntity savedEntity = payerRepository.save(payerEntity);
        return mapToPayerDto(savedEntity);
    }

    @Override
    public Optional<PayerDto> getPayerById(Long payerId) {
        return payerRepository.findById(payerId)
                .map(this::mapToPayerDto);
    }

    @Override
    public List<PayerDto> getAllPayers() {
        List<PayerEntity> payerEntities = payerRepository.findAll(); // Получаем список PayerEntity
        return payerEntities.stream()
                .map(this::mapToPayerDto)
                .collect(Collectors.toList());
    }

    @Override
    public PayerDto updatePayer(Long payerId, PayerDto updatedPayer) {
        PayerEntity payerEntity = payerRepository.findById(payerId)
                .orElseThrow(() -> new EntityNotFoundException("Payer not found"));

        payerEntity.setName(updatedPayer.name());
        payerEntity.setSecondName(updatedPayer.secondName());
        payerEntity.setBirthDate(updatedPayer.birthDate());
        payerEntity.setEmail(updatedPayer.email());
        payerEntity.setPhone(updatedPayer.phone());

        PayerEntity savedEntity = payerRepository.save(payerEntity);
        return mapToPayerDto(savedEntity);
    }


    @Autowired
    private InvoiceService invoiceService;

    @Override
    public List<InvoiceDto> getInvoicesByPayerId(Long payerId) {
        PayerEntity payerEntity = payerRepository.findById(payerId)
                .orElseThrow(() -> new EntityNotFoundException("Payer not found"));

        List<InvoiceEntity> invoices = payerEntity.getInvoices();

        return invoices.stream()
                .map(invoiceService::mapToInvoiceDto)
                .collect(Collectors.toList());
    }


    // Метод для преобразования PayerEntity в PayerDto
    public PayerDto mapToPayerDto(PayerEntity payerEntity) {
        return new PayerDto(
                payerEntity.getPayerId(),
                payerEntity.getName(),
                payerEntity.getSecondName(),
                payerEntity.getBirthDate(),
                payerEntity.getEmail(),
                payerEntity.getPhone()
        );
    }

    // Метод для преобразования PayerDto в PayerEntity
    public PayerEntity mapToPayerEntity(PayerDto payerDto) {
        return new PayerEntity(
                null, // payerId будет сгенерирован
                payerDto.name(),
                payerDto.secondName(),
                payerDto.birthDate(),
                payerDto.email(),
                payerDto.phone()
        );
    }
}
