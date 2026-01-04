package com.web.tilotoma.entity.material;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "vendor_payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_bill_id", nullable = false)
    private VendorBill vendorBill;

    @Column(nullable = false)
    private Double paidAmount;

    @Column(nullable = false)
    private String paymentMode; // CASH, UPI, BANK

    @Column(nullable = false)
    private LocalDate paymentDate;

    private String remarks;
}
