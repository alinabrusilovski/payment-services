package com.paymentservice.entity;

import com.paymentservice.dto.InvoiceDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payer_id")
    private Integer payerId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "second_name", nullable = false)
    private String secondName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @OneToMany(mappedBy = "payer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceEntity> invoices;

    public List<InvoiceEntity> getInvoices() {
        return invoices;
    }

    public PayerEntity(Integer payerId, String name, String secondName, LocalDate birthDate, String email, String phone) {
        this.payerId = payerId;
        this.name = name;
        this.secondName = secondName;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
    }

}
