package com.web.tilotoma.entity.material;
import com.web.tilotoma.entity.material.Vendor;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendor_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // ✅ AUTO ID

    @Column(name = "order_number", unique = true)
    private String orderNumber;   // ✅ ODR-{id}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "received_order")
    private Boolean receivedOrder = false;

    @Column(name = "approved_on")
    private LocalDate approvedOn;

    private LocalDate orderDate;
    private LocalDate requiredBy;
    private String remarks;
    private Double totalAmount;

    /*@OneToMany(mappedBy = "vendorOrder", cascade = CascadeType.ALL)
    private List<VendorOrderItem> items;*/


    @OneToMany(
            mappedBy = "vendorOrder",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<VendorOrderItem> items = new ArrayList<>();
}

