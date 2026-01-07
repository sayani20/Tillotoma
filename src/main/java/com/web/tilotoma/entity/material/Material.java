package com.web.tilotoma.entity.material;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "materials")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "material_name", nullable = false)
    private String materialName;

    @Column(nullable = false)
    private String unit;   // BAG, KG, CFT, NOS

    private String brand;

    @Column(name = "minimum_limit", nullable = false)
    private Double minimumLimit;

    // ✅ ONLY FK COLUMN (NO extra "category" column)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MaterialCategory materialCategory;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_on")
    private LocalDateTime createdOn;
}
