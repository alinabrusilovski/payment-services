package com.paymentservice.service;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.dto.InvoicePositionDto;
import com.paymentservice.dto.PayerDto;
import com.paymentservice.entity.InvoiceEntity;
import com.paymentservice.entity.InvoicePositionEntity;
import com.paymentservice.entity.PayerEntity;
import com.paymentservice.repository.InvoicePositionRepository;
import com.paymentservice.repository.InvoiceRepository;
import com.paymentservice.repository.PayerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    PayerRepository payerRepository;
    @Autowired
    InvoicePositionRepository invoicePositionRepository;

    //INVOICE

    //сохранение данных
    public InvoiceEntity createInvoice(InvoiceDto invoiceDto) {
        // Найти или создать плательщика
        PayerEntity payer = payerRepository.findById(invoiceDto.payer().payerId())
                .orElseGet(() -> {
                    PayerEntity newPayer = new PayerEntity();
                    newPayer.setName(invoiceDto.payer().name());
                    newPayer.setSecondName(invoiceDto.payer().secondName());
                    newPayer.setBirthDate(invoiceDto.payer().birthDate());
                    newPayer.setEmail(invoiceDto.payer().email());
                    newPayer.setPhone(invoiceDto.payer().phone());
                    return payerRepository.save(newPayer);
                });

        // Создание инвойса
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setSystemId(invoiceDto.systemId());
        invoiceEntity.setInvoiceDescription(invoiceDto.invoiceDescription());
        invoiceEntity.setPayer(payer);

        // Сохранение инвойса
        invoiceRepository.save(invoiceEntity);

        // Сохранение позиций инвойса
        if (invoiceDto.positions() != null) {
            for (var positionDto : invoiceDto.positions()) {
                InvoicePositionEntity position = new InvoicePositionEntity();
                position.setInvoice(invoiceEntity);
                position.setInvoicePositionDescription(positionDto.invoicePositionDescription());
                position.setAmount(positionDto.amount());
                invoiceEntity.getPositions().add(position);
            }
            invoiceRepository.save(invoiceEntity);  // Обновляем инвойс с позициями
        }

        return invoiceEntity;
    }

    public List<InvoiceEntity> getAllInvoices() {
        return invoiceRepository.findAll();
    }



    // Преобразование PayerEntity в PayerDto
    public InvoiceDto mapToInvoiceDto(InvoiceEntity invoiceEntity) {
        PayerDto payerDto = new PayerDto(
                invoiceEntity.getPayer().getPayerId(),
                invoiceEntity.getPayer().getName(),
                invoiceEntity.getPayer().getSecondName(),
                invoiceEntity.getPayer().getBirthDate(),
                invoiceEntity.getPayer().getEmail(),
                invoiceEntity.getPayer().getPhone()
        );

        // Преобразование позиций инвойса
        List<InvoicePositionDto> positionDtos = invoiceEntity.getPositions().stream()
                .map(positionEntity -> new InvoicePositionDto(
                        positionEntity.getInvoicePositionId(),
                        positionEntity.getInvoicePositionDescription(),
                        positionEntity.getAmount()
                ))
                .collect(Collectors.toList());

        return new InvoiceDto(
                invoiceEntity.getSystemId(),  // Убедись, что это Integer
                invoiceEntity.getInvoiceDescription(),
                payerDto,
                positionDtos
        );
    }

    //PAYER

    public PayerDto createPayer(PayerDto payerDto) {
        PayerEntity payerEntity = mapToPayerEntity(payerDto);
        PayerEntity savedEntity = payerRepository.save(payerEntity);
        return mapToPayerDto(savedEntity);
    }


    public Optional<PayerDto> getPayerById(Integer payerId) {
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
    public PayerDto updatePayer(Integer payerId, PayerDto updatedPayer) {
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

    @Override
    public List<InvoiceDto> getInvoicesByPayerId(Integer payerId) {
        PayerEntity payerEntity = payerRepository.findById(payerId)
                .orElseThrow(() -> new EntityNotFoundException("Payer not found"));

        List<InvoiceEntity> invoices = payerEntity.getInvoices();

        if (invoices.isEmpty()) {
            return List.of();
        }

        return invoices.stream()
                .map(this::mapToInvoiceDto)
                .collect(Collectors.toList());
    }


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


    //INVOICEPOSITION


    public InvoicePositionDto createInvoicePosition(InvoicePositionDto positionDto, Integer invoiceId) {
        InvoicePositionEntity positionEntity = new InvoicePositionEntity();
        positionEntity.setInvoicePositionDescription(positionDto.invoicePositionDescription());
        positionEntity.setAmount(positionDto.amount());

        // Здесь можно найти инвойс по ID и установить его в позицию
        InvoiceEntity invoiceEntity = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        positionEntity.setInvoice(invoiceEntity); // Устанавливаем инвойс

        InvoicePositionEntity savedPosition = invoicePositionRepository.save(positionEntity);
        return mapToInvoicePositionDto(savedPosition);
    }

    public List<InvoicePositionDto> getInvoicePositionsByInvoiceId(Integer invoiceId) {
        List<InvoicePositionEntity> positions = invoicePositionRepository.findByInvoice_InvoiceId(invoiceId);
        return positions.stream()
                .map(this::mapToInvoicePositionDto)
                .collect(Collectors.toList());
    }

    public InvoicePositionDto getInvoicePositionById(Integer positionId) {
        InvoicePositionEntity positionEntity = invoicePositionRepository.findById(positionId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice Position not found"));
        return mapToInvoicePositionDto(positionEntity);
    }

    public InvoicePositionDto updateInvoicePosition(Integer positionId, InvoicePositionDto positionDto) {
        InvoicePositionEntity positionEntity = invoicePositionRepository.findById(positionId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice Position not found"));

        positionEntity.setInvoicePositionDescription(positionDto.invoicePositionDescription());
        positionEntity.setAmount(positionDto.amount());

        InvoicePositionEntity updatedPosition = invoicePositionRepository.save(positionEntity);
        return mapToInvoicePositionDto(updatedPosition);
    }

    public void deleteInvoicePosition(Integer positionId) {
        if (!invoicePositionRepository.existsById(positionId)) {
            throw new EntityNotFoundException("Invoice Position not found");
        }
        invoicePositionRepository.deleteById(positionId);
    }

    public InvoicePositionDto mapToInvoicePositionDto(InvoicePositionEntity positionEntity) {
        return new InvoicePositionDto(
                positionEntity.getInvoicePositionId(),
                positionEntity.getInvoicePositionDescription(),
                positionEntity.getAmount()
        );
    }


}
