package com.web.tilotoma.entity.material;
import com.web.tilotoma.entity.material.Material;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vendor_order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private VendorOrder vendorOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id")
    private Material material;

    private String brand;

    private String unit;

    private Double quantity;

    private Double rate;

    @Column(name = "net_amount")
    private Double netAmount;
}
