package com.web.tilotoma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ConsumptionResponseDto {

    private Long consumptionId;

    private Long materialId;
    private String materialName;
    private String unit;

    private Double consumedQuantity;
    private Double availableStockAfter;

    private String tower;
    private String area;

    private LocalDateTime consumptionDate;
}
