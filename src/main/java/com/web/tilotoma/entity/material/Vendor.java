package com.web.tilotoma.entity.material;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.web.tilotoma.entity.material.Material;
import com.web.tilotoma.entity.material.MaterialCategory;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vendorName;

    private String contactPersonName;

    private String mobile;

    private String emailId;

    private String gstNumber;

    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_category_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MaterialCategory materialCategory;

    @ManyToMany
    @JoinTable(
            name = "vendor_materials",
            joinColumns = @JoinColumn(name = "vendor_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id")
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Material> materials;

    @Column(name = "isActive")
    private Boolean isActive = true;

    @Column(name = "created_on")
    private LocalDateTime createdOn;
}