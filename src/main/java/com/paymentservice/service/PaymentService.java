package com.paymentservice.service;

import com.paymentservice.dto.OperationResult;
import com.paymentservice.entity.InvoicePositionEntity;
import com.paymentservice.entity.PaymentEntity;
import com.paymentservice.entity.PaymentStatusEntity;
import com.paymentservice.repository.InvoicePositionRepository;
import com.paymentservice.repository.PaymentRepository;
import com.paymentservice.repository.PaymentStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Component
@Slf4j
public class PaymentService implements IPaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final InvoicePositionRepository invoicePositionRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, PaymentStatusRepository paymentStatusRepository, InvoicePositionRepository invoicePositionRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentStatusRepository = paymentStatusRepository;
        this.invoicePositionRepository = invoicePositionRepository;
    }

    @Override
    @Transactional
    public OperationResult<PaymentEntity> createPayment(List<Integer> invoicePositionIds, BigDecimal amount) {
        List<InvoicePositionEntity> positions = invoicePositionRepository.findAllById(invoicePositionIds);

        BigDecimal total = positions.stream()
                .map(InvoicePositionEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (!total.equals(amount)) {
            return OperationResult.failure("Amount mismatch with invoice positions");
        }

        PaymentEntity payment = new PaymentEntity();
        payment.setAmount(amount);
        payment.setStatus("CREATED");
        payment.setCreated(OffsetDateTime.now());
        payment.setInvoicePositions(positions);

        PaymentEntity savedPayment = paymentRepository.save(payment);

        PaymentStatusEntity paymentStatus = new PaymentStatusEntity();
        paymentStatus.setPayment(savedPayment);
        paymentStatus.setStatus("CREATED");
        paymentStatus.setStatusDate(OffsetDateTime.now());
        paymentStatusRepository.save(paymentStatus);

        return OperationResult.success(savedPayment);
    }
}
