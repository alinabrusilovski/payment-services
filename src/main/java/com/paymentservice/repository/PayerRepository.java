package com.paymentservice.repository;

import com.paymentservice.dto.PayerDto;
import com.paymentservice.entity.PayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayerRepository extends JpaRepository<PayerEntity, Integer> {
}