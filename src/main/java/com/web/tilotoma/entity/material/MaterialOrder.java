package com.web.tilotoma.entity.material;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "material_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(nullable = false)
    private LocalDate orderDate;

    @Column(nullable = false)
    private String status;
    // PENDING, APPROVED, PARTIAL, COMPLETED

    private String createdBy;
    private String approvedBy;
}
