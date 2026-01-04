package com.web.tilotoma.entity.material;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "vendor_materials",
        uniqueConstraints = @UniqueConstraint(columnNames = {"vendor_id", "material_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    private Double lastPurchaseRate;

    @Column(nullable = false)
    private Boolean isActive = true;
}
