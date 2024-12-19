package com.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;

@Entity
@Table(name = "payment_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private PaymentEntity payment;

    @Column(nullable = false)
    private String status; // CREATED, REFUND

    @Column(name = "status_date", nullable = false)
    private OffsetDateTime statusDate;
}
