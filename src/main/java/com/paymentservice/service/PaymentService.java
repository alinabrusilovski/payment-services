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
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService implements IPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    PayerRepository payerRepository;
    @Autowired
    InvoicePositionRepository invoicePositionRepository;


    //INVOICE

    @Override
    @Transactional
    public InvoiceEntity createInvoice(InvoiceDto invoiceDto) {
        logger.info("Starting invoice creation process for systemId: {}", invoiceDto.systemId());

        try {
            // Создание и сохранение плательщика
            PayerEntity payer = createAndSavePayer(invoiceDto.payer());

            // Создание и сохранение инвойса
            InvoiceEntity invoiceEntity = createAndSaveInvoice(invoiceDto, payer);

            // Обработка позиций инвойса
            processInvoicePositions(invoiceEntity, invoiceDto.positions());

            logger.info("Invoice creation process completed successfully for systemId: {}", invoiceDto.systemId());
            return invoiceEntity;

        } catch (Exception e) {
            logger.error("Error occurred during invoice creation for systemId {}: {}", invoiceDto.systemId(), e.getMessage(), e);
            throw new RuntimeException("Error occurred during invoice creation", e);
        }
    }

    private PayerEntity createAndSavePayer(PayerDto payerDto) {
        PayerEntity newPayer = new PayerEntity();
        newPayer.setName(payerDto.name());
        newPayer.setSecondName(payerDto.secondName());
        newPayer.setBirthDate(payerDto.birthDate());
        newPayer.setEmail(payerDto.email());
        newPayer.setPhone(payerDto.phone());

        logger.info("Payer data created: {} {}", newPayer.getName(), newPayer.getSecondName());

        PayerEntity savedPayer = payerRepository.save(newPayer);
        logger.info("Payer saved with ID: {}", savedPayer.getPayerId());

        return savedPayer;
    }

    private InvoiceEntity createAndSaveInvoice(InvoiceDto invoiceDto, PayerEntity payer) {
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setSystemId(invoiceDto.systemId());
        invoiceEntity.setInvoiceDescription(invoiceDto.invoiceDescription());
        invoiceEntity.setPayer(payer);

        logger.info("Invoice object created for systemId: {}", invoiceDto.systemId());

        InvoiceEntity savedInvoice = invoiceRepository.save(invoiceEntity);
        logger.info("Invoice saved with ID: {}", savedInvoice.getInvoiceId());

        return savedInvoice;
    }

    private void processInvoicePositions(InvoiceEntity invoiceEntity, List<InvoicePositionDto> positionDtos) {
        if (positionDtos == null || positionDtos.isEmpty()) {
            logger.warn("No invoice positions provided for invoice with systemId: {}", invoiceEntity.getSystemId());
            return;
        }

        logger.info("Processing {} invoice positions for invoice ID: {}", positionDtos.size(), invoiceEntity.getInvoiceId());

        for (var positionDto : positionDtos) {
            InvoicePositionEntity position = new InvoicePositionEntity();
            position.setInvoicePositionDescription(positionDto.invoicePositionDescription());
            position.setAmount(positionDto.amount());
            position.setInvoice(invoiceEntity);

            invoiceEntity.getPositions().add(position);

            invoicePositionRepository.save(position);
            logger.info("Invoice position saved for invoice ID: {}", invoiceEntity.getInvoiceId());
        }
    }


    @Override
    public List<InvoiceEntity> getAllInvoices() {
        logger.info("Fetching all invoices from the database...");

        try {
            List<InvoiceEntity> invoices = invoiceRepository.findAll();

            if (invoices.isEmpty()) {
                logger.info("No invoices found in the database.");
            } else {
                logger.info("Successfully fetched {} invoices from the database.", invoices.size());
            }

            return invoices;

        } catch (Exception e) {
            logger.error("Error fetching invoices from the database: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching invoices.", e);
        }
    }

}


//    //PAYER
//
// //Преобразование PayerEntity в PayerDto
//public InvoiceDto mapToInvoiceDto(InvoiceEntity invoiceEntity) {
//    PayerDto payerDto = new PayerDto(
//            invoiceEntity.getPayer().getPayerId(),
//            invoiceEntity.getPayer().getName(),
//            invoiceEntity.getPayer().getSecondName(),
//            invoiceEntity.getPayer().getBirthDate(),
//            invoiceEntity.getPayer().getEmail(),
//            invoiceEntity.getPayer().getPhone()
//    );
//
//    // Преобразование позиций инвойса
//    List<InvoicePositionDto> positionDtos = invoiceEntity.getPositions().stream()
//            .map(positionEntity -> new InvoicePositionDto(
//                    positionEntity.getInvoicePositionId(),
//                    positionEntity.getInvoicePositionDescription(),
//                    positionEntity.getAmount()
//            ))
//            .collect(Collectors.toList());
//
//    return new InvoiceDto(
//            invoiceEntity.getSystemId(),  // Убедись, что это Integer
//            invoiceEntity.getInvoiceDescription(),
//            payerDto,
//            positionDtos
//    );
//}

//    @Override
//    public PayerDto createPayer(PayerDto payerDto) {
//        PayerEntity payerEntity = mapToPayerEntity(payerDto);
//        PayerEntity savedEntity = payerRepository.save(payerEntity);
//        return mapToPayerDto(savedEntity);
//    }
//
//
//    @Override
//    public Optional<PayerDto> getPayerById(Integer payerId) {
//        return payerRepository.findById(payerId)
//                .map(this::mapToPayerDto);
//    }
//
//    @Override
//    public List<PayerDto> getAllPayers() {
//        List<PayerEntity> payerEntities = payerRepository.findAll(); // Получаем список PayerEntity
//        return payerEntities.stream()
//                .map(this::mapToPayerDto)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public PayerDto updatePayer(Integer payerId, PayerDto updatedPayer) {
//        PayerEntity payerEntity = payerRepository.findById(payerId)
//                .orElseThrow(() -> new EntityNotFoundException("Payer not found"));
//
//        payerEntity.setName(updatedPayer.name());
//        payerEntity.setSecondName(updatedPayer.secondName());
//        payerEntity.setBirthDate(updatedPayer.birthDate());
//        payerEntity.setEmail(updatedPayer.email());
//        payerEntity.setPhone(updatedPayer.phone());
//
//        PayerEntity savedEntity = payerRepository.save(payerEntity);
//        return mapToPayerDto(savedEntity);
//    }
//
//    @Override
//    public List<InvoiceDto> getInvoicesByPayerId(Integer payerId) {
//        PayerEntity payerEntity = payerRepository.findById(payerId)
//                .orElseThrow(() -> new EntityNotFoundException("Payer not found"));
//
//        List<InvoiceEntity> invoices = payerEntity.getInvoices();
//
//        if (invoices.isEmpty()) {
//            return List.of();
//        }
//
//        return invoices.stream()
//                .map(this::mapToInvoiceDto)
//                .collect(Collectors.toList());
//    }
//
//
//    public PayerDto mapToPayerDto(PayerEntity payerEntity) {
//        return new PayerDto(
//                payerEntity.getPayerId(),
//                payerEntity.getName(),
//                payerEntity.getSecondName(),
//                payerEntity.getBirthDate(),
//                payerEntity.getEmail(),
//                payerEntity.getPhone()
//        );
//    }
//
//    public PayerEntity mapToPayerEntity(PayerDto payerDto) {
//        return new PayerEntity(
//                null, // payerId будет сгенерирован
//                payerDto.name(),
//                payerDto.secondName(),
//                payerDto.birthDate(),
//                payerDto.email(),
//                payerDto.phone()
//        );
//    }
//
//
//    //INVOICEPOSITION
//
//
//    @Override
//    public InvoicePositionDto createInvoicePosition(InvoicePositionDto positionDto, Integer invoiceId) {
//        InvoicePositionEntity positionEntity = new InvoicePositionEntity();
//        positionEntity.setInvoicePositionDescription(positionDto.invoicePositionDescription());
//        positionEntity.setAmount(positionDto.amount());
//
//        // Здесь можно найти инвойс по ID и установить его в позицию
//        InvoiceEntity invoiceEntity = invoiceRepository.findById(invoiceId)
//                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));
//
//        positionEntity.setInvoice(invoiceEntity); // Устанавливаем инвойс
//
//        InvoicePositionEntity savedPosition = invoicePositionRepository.save(positionEntity);
//        return mapToInvoicePositionDto(savedPosition);
//    }
//
//    @Override
//    public List<InvoicePositionDto> getInvoicePositionsByInvoiceId(Integer invoiceId) {
//        List<InvoicePositionEntity> positions = invoicePositionRepository.findByInvoice_InvoiceId(invoiceId);
//        return positions.stream()
//                .map(this::mapToInvoicePositionDto)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public InvoicePositionDto getInvoicePositionById(Integer positionId) {
//        InvoicePositionEntity positionEntity = invoicePositionRepository.findById(positionId)
//                .orElseThrow(() -> new EntityNotFoundException("Invoice Position not found"));
//        return mapToInvoicePositionDto(positionEntity);
//    }
//
//    @Override
//    public InvoicePositionDto updateInvoicePosition(Integer positionId, InvoicePositionDto positionDto) {
//        InvoicePositionEntity positionEntity = invoicePositionRepository.findById(positionId)
//                .orElseThrow(() -> new EntityNotFoundException("Invoice Position not found"));
//
//        positionEntity.setInvoicePositionDescription(positionDto.invoicePositionDescription());
//        positionEntity.setAmount(positionDto.amount());
//
//        InvoicePositionEntity updatedPosition = invoicePositionRepository.save(positionEntity);
//        return mapToInvoicePositionDto(updatedPosition);
//    }
//
//    @Override
//    public void deleteInvoicePosition(Integer positionId) {
//        if (!invoicePositionRepository.existsById(positionId)) {
//            throw new EntityNotFoundException("Invoice Position not found");
//        }
//        invoicePositionRepository.deleteById(positionId);
//    }
//
//    public InvoicePositionDto mapToInvoicePositionDto(InvoicePositionEntity positionEntity) {
//        return new InvoicePositionDto(
//                positionEntity.getInvoicePositionId(),
//                positionEntity.getInvoicePositionDescription(),
//                positionEntity.getAmount()
//        );
//    }