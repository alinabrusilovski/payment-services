package com.paymentservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "invoice")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "system_id")
    private Long systemId;

    @ManyToOne
    @JoinColumn(name = "payer_id", nullable = false)
    private PayerEntity payer;

    @Column(name = "invoice_description")
    private String invoiceDescription;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoicePositionEntity> invoicePositions;

}