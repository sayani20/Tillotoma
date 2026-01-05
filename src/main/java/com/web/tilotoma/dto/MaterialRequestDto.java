package com.web.tilotoma.dto;

import lombok.Data;

@Data
public class MaterialRequestDto {
    private String materialName;
    private String unit;          // BAG, KG, CFT
    private String category;      // Cement, Sand
    private Double minimumLimit;
}
