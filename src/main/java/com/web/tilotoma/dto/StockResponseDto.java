package com.web.tilotoma.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockResponseDto {

    private Long materialId;
    private String materialName;
    private String unit;

    // ✅ NEW
    private String brand;
    private LocalDateTime lastUpdatedOn;

    private Double availableQuantity;
    private Double minimumQuantity;

    // 🔹 constructor for JPQL projection
    public StockResponseDto(
            Long materialId,
            String materialName,
            String unit,
            String brand,
            LocalDateTime lastUpdatedOn,
            Double availableQuantity,
            Double minimumQuantity
    ) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.unit = unit;
        this.brand = brand;
        this.lastUpdatedOn = lastUpdatedOn;
        this.availableQuantity = availableQuantity;
        this.minimumQuantity = minimumQuantity;
    }
}
