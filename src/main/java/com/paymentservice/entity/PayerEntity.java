package com.paymentservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
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
    @Column(name = "id")
    private Integer payerId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "second_name", nullable = false)
    private String secondName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "email")
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
