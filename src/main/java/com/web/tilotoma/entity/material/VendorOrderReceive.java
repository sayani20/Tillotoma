package com.web.tilotoma.entity.material;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vendor_order_receives")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorOrderReceive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Order reference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private VendorOrder vendorOrder;

    // 🔗 Material reference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    // 📦 Receive details
    private Double receivedQuantity;

    private Double receivedRate;

    private Double receivedAmount;

    // 🧾 Offline bill / challan number
    @Column(name = "challan_number", nullable = false)
    private String challanNumber;

    // 🗓️ Receive time
    private LocalDateTime receivedOn;
    @Column(name = "order_received_type")
    private String orderReceivedType;
}
