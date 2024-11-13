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
import com.paymentservice.validation.IValidator;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PaymentService implements IPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final InvoiceRepository invoiceRepository;
    private final PayerRepository payerRepository;
    private final InvoicePositionRepository invoicePositionRepository;
    private final IValidator<InvoiceDto> invoiceDtoValidator;
    private final IValidator<PayerDto> payerDtoValidator;
    private final IValidator<List<InvoicePositionDto>> invoicePositionValidator;

    @Autowired
    public PaymentService(
            InvoiceRepository invoiceRepository,
            PayerRepository payerRepository,
            InvoicePositionRepository invoicePositionRepository,
            IValidator<InvoiceDto> invoiceDtoValidator,
            IValidator<PayerDto> payerDtoValidator,
            IValidator<List<InvoicePositionDto>> invoicePositionValidator
    ) {
        this.invoiceRepository = invoiceRepository;
        this.payerRepository = payerRepository;
        this.invoicePositionRepository = invoicePositionRepository;
        this.invoiceDtoValidator = invoiceDtoValidator;
        this.payerDtoValidator = payerDtoValidator;
        this.invoicePositionValidator = invoicePositionValidator;
    }

    @Override
    @Transactional
    public InvoiceEntity createInvoice(InvoiceDto invoiceDto) {

        invoiceDtoValidator.validate(invoiceDto);
        payerDtoValidator.validate(invoiceDto.payer());
        invoicePositionValidator.validate(invoiceDto.positions());

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
        newPayer.setName(payerDto.getName());
        newPayer.setSecondName(payerDto.getSecondName());
        newPayer.setBirthDate(payerDto.getBirthDate());
        newPayer.setEmail(payerDto.getEmail());
        newPayer.setPhone(payerDto.getPhone());

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
