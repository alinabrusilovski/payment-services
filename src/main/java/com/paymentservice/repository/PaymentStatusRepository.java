package com.paymentservice.repository;

import com.paymentservice.entity.PaymentStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatusEntity, Integer> {
}
