package com.web.tilotoma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ConsumptionResponseDto {

    private Long materialId;
    private Double consumedQty;
    private Double remainingStock;
}
