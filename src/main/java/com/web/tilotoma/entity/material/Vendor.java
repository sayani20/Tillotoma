package com.web.tilotoma.entity.material;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vendors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String vendorName;

    private String contactPersonName;

    @Column(length=15)
    private String mobile;

    @Column(length=100)
    private String emailId;

    private String gstNumber;
    private String address;

    @Column(nullable=false)
    private Boolean isActive=true;

    // 🔥 NEW
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="material_category_id", nullable=false)
    private MaterialCategory materialCategory;

    // vendor ওই category-এর material দেবে
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "vendor_materials",
            joinColumns = @JoinColumn(name="vendor_id"),
            inverseJoinColumns = @JoinColumn(name="material_id")
    )
    private List<Material> materials;

    @Column(name="created_on")
    private LocalDateTime createdOn;
}
