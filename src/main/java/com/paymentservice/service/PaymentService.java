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
import com.paymentservice.validation.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
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


    @Autowired
    public PaymentService(
            InvoiceRepository invoiceRepository,
            PayerRepository payerRepository,
            InvoicePositionRepository invoicePositionRepository,
            IValidator<InvoiceDto> invoiceDtoValidator
    ) {
        this.invoiceRepository = invoiceRepository;
        this.payerRepository = payerRepository;
        this.invoicePositionRepository = invoicePositionRepository;
        this.invoiceDtoValidator = invoiceDtoValidator;
    }

    @Override
    @Transactional
    public ValidationResult<InvoiceEntity> createInvoice(InvoiceDto invoiceDto) {

        ValidationResult<InvoiceDto> validationResult = invoiceDtoValidator.validate(invoiceDto);

        if (!validationResult.isSuccess()) {
            return ValidationResult.failure(validationResult.getError().toString());
        }

        logger.info("Starting invoice creation process for systemId: {}", invoiceDto.systemId());

        try {
            PayerEntity payer = createAndSavePayer(invoiceDto.payer());
            InvoiceEntity invoiceEntity = createAndSaveInvoice(invoiceDto, payer);
            processInvoicePositions(invoiceEntity, invoiceDto.positions());

            logger.info("Invoice creation process completed successfully for systemId: {}", invoiceDto.systemId());
            return ValidationResult.success(invoiceEntity);

        } catch (Exception e) {
            logger.error("Error occurred during invoice creation for systemId {}: {}", invoiceDto.systemId(), e.getMessage(), e);
            return ValidationResult.failure("Error occurred during invoice creation: " + e.getMessage());
        }
    }

    private PayerEntity createAndSavePayer(PayerDto payerDto) {
        logger.info("Creating payer");

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

    private InvoiceEntity createAndSaveInvoice(InvoiceDto invoiceDto, PayerEntity payer) {
        logger.info("Creating invoice");

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
        logger.info("Processing positions");

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
    public ValidationResult<List<InvoiceEntity>> getAllInvoices() {
        logger.info("Fetching all invoices from the database");

        try {
            List<InvoiceEntity> invoices = invoiceRepository.findAll();

            if (invoices.isEmpty()) {
                return ValidationResult.failure("No invoices found");
            } else {
                return ValidationResult.success(invoices);
            }

        } catch (Exception e) {
            logger.error("Error fetching invoices from the database: {}", e.getMessage(), e);
            return ValidationResult.failure("Error occurred while fetching invoices: " + e.getMessage());
        }
    }

}
