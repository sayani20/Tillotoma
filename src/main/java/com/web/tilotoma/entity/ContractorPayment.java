package com.web.tilotoma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "contractor_payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractorPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // কোন contractor কে payment করা হয়েছে
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_id")
    private Contractor contractor;

    // Billing row এর billNo attach করবো
    @Column(nullable = false)
    private String billNo;

    // Billing এর তারিখ (same billDate)
    @Column(nullable = false)
    private LocalDate billDate;

    // Total bill amount (reference)
    @Column(nullable = false)
    private Double totalAmount;

    // Admin কত টাকা দিল
    @Column(nullable = false)
    private Double paidAmount;

    // Payment date (admin যেদিন payment দিল)
    @Column(nullable = false)
    private LocalDate paymentDate;

    // Notes (optional)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;
}
