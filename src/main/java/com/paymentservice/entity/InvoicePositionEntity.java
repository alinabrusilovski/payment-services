package com.paymentservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_position")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class InvoicePositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_position_id")
    private Integer invoicePositionId;

    @Column(name = "invoice_position_description")
    private String invoicePositionDescription;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private InvoiceEntity invoice;

}