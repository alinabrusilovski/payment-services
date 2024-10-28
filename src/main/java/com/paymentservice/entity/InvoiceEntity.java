package com.paymentservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.paymentservice.dto.PayerDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoice")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "positions")
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Integer invoiceId;

    @Column(name = "system_id")
    private Integer systemId;

    @Column(name = "invoice_description")
    private String invoiceDescription;

    @ManyToOne
    @JoinColumn(name = "payer_id", nullable = false)
    private PayerEntity payer;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<InvoicePositionEntity> positions = new ArrayList<>();
}