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
import com.paymentservice.validation.InvoiceDtoValidator;
import com.paymentservice.validation.InvoicePositionDtoValidator;
import com.paymentservice.validation.PayerDtoValidator;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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
    @Autowired
    private InvoiceDtoValidator invoiceDtoValidator;
    @Autowired
    private PayerDtoValidator payerDtoValidator;
    @Autowired
    private InvoicePositionDtoValidator invoicePositionValidator;


    @Override
    @Transactional
    public InvoiceEntity createInvoice(InvoiceDto invoiceDto) {

        invoiceDtoValidator.validate(invoiceDto);
        payerDtoValidator.validate(invoiceDto.payer());
        invoicePositionValidator.validatePositions(invoiceDto.positions()); // Валидируем позиции

        String relationId = UUID.randomUUID().toString();
        MDC.put("relationId", relationId);

        logger.info("Starting invoice creation process for systemId: {}", invoiceDto.systemId());

        try {

            PayerEntity payer = createAndSavePayer(invoiceDto.payer(), relationId);

            InvoiceEntity invoiceEntity = createAndSaveInvoice(invoiceDto, payer, relationId);

            processInvoicePositions(invoiceEntity, invoiceDto.positions(), relationId);

            logger.info("Invoice creation process completed successfully for systemId: {}", invoiceDto.systemId());
            return invoiceEntity;

        } catch (Exception e) {
            logger.error("Error occurred during invoice creation for systemId {}: {}", invoiceDto.systemId(), e.getMessage(), e);
            throw new RuntimeException("Error occurred during invoice creation", e);
        } finally {
            MDC.clear();
        }
    }

    private PayerEntity createAndSavePayer(PayerDto payerDto, String relationId) {
        logger.info("Creating payer with relationId: {}", relationId);

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

    private InvoiceEntity createAndSaveInvoice(InvoiceDto invoiceDto, PayerEntity payer, String relationId) {
        logger.info("Creating invoice with relationId: {}", relationId);

        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setSystemId(invoiceDto.systemId());
        invoiceEntity.setInvoiceDescription(invoiceDto.invoiceDescription());
        invoiceEntity.setPayer(payer);

        logger.info("Invoice object created for systemId: {}", invoiceDto.systemId());

        InvoiceEntity savedInvoice = invoiceRepository.save(invoiceEntity);
        logger.info("Invoice saved with ID: {}", savedInvoice.getInvoiceId());

        return savedInvoice;
    }

    private void processInvoicePositions(InvoiceEntity invoiceEntity, List<InvoicePositionDto> positionDtos, String relationId) {
        logger.info("Processing positions for invoice with relationId: {}", relationId);

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
