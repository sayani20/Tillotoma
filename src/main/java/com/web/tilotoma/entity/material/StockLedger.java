package com.web.tilotoma.entity.material;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_ledger")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Material
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    // 🔗 Optional order reference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private VendorOrder vendorOrder;

    @Enumerated(EnumType.STRING)
    private StockTxnType txnType; // IN / OUT

    private Double quantity;

    private Double rate;

    private String reference; // ORDER_RECEIVE / ISSUE / ADJUSTMENT

    private LocalDateTime txnDate;
}
