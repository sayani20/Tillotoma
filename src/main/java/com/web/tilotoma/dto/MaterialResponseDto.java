package com.web.tilotoma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MaterialResponseDto {
    private Long id;
    private String materialName;
    private String unit;
    private String category;
    private Double minimumLimit;
    private Boolean isActive;
}
