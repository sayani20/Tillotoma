package com.web.tilotoma.entity.material;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false, unique = true)
    private String materialName;

    @Column(nullable = false)
    private String unit; // BAG, KG, CFT

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Double minimumLimit;

    @Column(nullable = false)
    private Boolean isActive = true;
}
