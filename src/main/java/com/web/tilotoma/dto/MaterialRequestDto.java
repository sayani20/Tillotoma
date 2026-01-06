package com.web.tilotoma.dto;

import lombok.Data;

@Data
public class MaterialRequestDto {
    private Long id;
    private String materialName;
    private String unit;          // BAG, KG, CFT
    private String materialCategory;      // Cement, Sand
    private Double minimumLimit;
    private String brand;
}
