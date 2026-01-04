package com.web.tilotoma.entity.labour;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "contractor_payment_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractorPaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_payment_id", nullable = false)
    private ContractorPayment contractorPayment;

    // এই update এ admin কত দিল
    @Column(nullable = false)
    private Double paidAmount;

    @Column(nullable = false)
    private LocalDate paymentDate;

    // 🔥 payment method as STRING
    @Column(nullable = false, length = 50)
    private String paymentMethod;
    // example: CASH, UPI, BANK, CHEQUE

    private String remarks;

}
