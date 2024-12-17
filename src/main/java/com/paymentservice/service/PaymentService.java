package com.paymentservice.service;

import com.paymentservice.dto.InvoiceDto;
import com.paymentservice.dto.InvoicePositionDto;
import com.paymentservice.dto.JsonWrapper;
import com.paymentservice.dto.OperationResult;
import com.paymentservice.dto.PayerDto;
import com.paymentservice.entity.InvoiceEntity;
import com.paymentservice.entity.InvoicePositionEntity;
import com.paymentservice.entity.PayerEntity;
import com.paymentservice.repository.InvoicePositionRepository;
import com.paymentservice.repository.InvoiceRepository;
import com.paymentservice.repository.PayerRepository;
import com.paymentservice.validation.IValidator;
import com.paymentservice.validation.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class PaymentService implements IPaymentService {

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
    public OperationResult<InvoiceEntity> createInvoice(InvoiceDto invoiceDto) {

        ValidationResult<InvoiceDto> validationResult = invoiceDtoValidator.validate(invoiceDto);

        if (!validationResult.isSuccess()) {
            return OperationResult.failure(validationResult.getError().toString());
        }

        log.info("Starting invoice creation process for systemId: {}", invoiceDto.systemId());

        try {
            PayerEntity payer = createAndSavePayer(invoiceDto.payer());
            InvoiceEntity invoiceEntity = createAndSaveInvoice(invoiceDto, payer);
            processInvoicePositions(invoiceEntity, invoiceDto.positions());

            log.info("Invoice creation process completed successfully for systemId: {}", invoiceDto.systemId());
            return OperationResult.success(invoiceEntity);

        } catch (Exception e) {
            log.error("Error occurred during invoice creation for systemId {}: {}", invoiceDto.systemId(), e.getMessage(), e);
            return OperationResult.failure("Error occurred during invoice creation: " + e.getMessage());
        }
    }

    private PayerEntity createAndSavePayer(PayerDto payerDto) {
        log.info("Creating payer");

        PayerEntity newPayer = new PayerEntity();
        newPayer.setName(payerDto.getName());
        newPayer.setSecondName(payerDto.getSecondName());
        newPayer.setBirthDate(payerDto.getBirthDate());
        newPayer.setEmail(payerDto.getEmail());
        newPayer.setPhone(payerDto.getPhone());

        log.info("Payer data created: {} {}", newPayer.getName(), newPayer.getSecondName());

        PayerEntity savedPayer = payerRepository.save(newPayer);
        log.info("Payer saved with ID: {}", savedPayer.getPayerId());

        return savedPayer;
    }

    private InvoiceEntity createAndSaveInvoice(InvoiceDto invoiceDto, PayerEntity payer) {
        log.info("Creating invoice");

        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setSystemId(invoiceDto.systemId());
        invoiceEntity.setInvoiceDescription(invoiceDto.invoiceDescription());
        invoiceEntity.setPayer(payer);

        log.info("Invoice object created for systemId: {}", invoiceDto.systemId());

        InvoiceEntity savedInvoice = invoiceRepository.save(invoiceEntity);
        log.info("Invoice saved with ID: {}", savedInvoice.getInvoiceId());

        return savedInvoice;
    }

    private void processInvoicePositions(InvoiceEntity invoiceEntity, List<InvoicePositionDto> positionDtos) {
        log.info("Processing positions");

        log.info("Processing {} invoice positions for invoice ID: {}", positionDtos.size(), invoiceEntity.getInvoiceId());

        for (var positionDto : positionDtos) {
            InvoicePositionEntity position = new InvoicePositionEntity();
            position.setInvoicePositionDescription(positionDto.invoicePositionDescription());
            position.setAmount(positionDto.amount());
            position.setInvoice(invoiceEntity);

            invoiceEntity.getPositions().add(position);

            invoicePositionRepository.save(position);
            log.info("Invoice position saved for invoice ID: {}", invoiceEntity.getInvoiceId());
        }
    }


    @Override
    public OperationResult<JsonWrapper<List<InvoiceEntity>>> getAllInvoices(int offset, int limit, String sortBy, String sortDirection) {
        log.info("Fetching invoices with offset={}, limit={}, sortBy={}, sortDirection={}", offset, limit, sortBy, sortDirection);

        if (limit > 1000) {
            log.warn("Limit exceeded. Limiting to 1000");
            limit = 1000;
        }

        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "invoiceId";
        }
        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(sortDirection);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.warn("Invalid or missing sortDirection '{}', defaulting to ASC", sortDirection);
            direction = Sort.Direction.ASC;
        }

        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(offset, limit, sort);
        Page<InvoiceEntity> page = invoiceRepository.findAll(pageable);

        JsonWrapper<List<InvoiceEntity>> wrapper = new JsonWrapper<>(page.getContent());
        log.info("Successfully fetched {} invoices.", wrapper.getValue().size());
        return OperationResult.success(wrapper);
    }
}
