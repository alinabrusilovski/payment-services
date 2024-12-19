package com.paymentservice.service;

import com.paymentservice.dto.OperationResult;
import com.paymentservice.entity.PaymentEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface IPaymentService {
    OperationResult<PaymentEntity> createPayment(List<Integer> invoicePositionIds, BigDecimal amount);
}
