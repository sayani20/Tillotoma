package com.web.tilotoma.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ConsumptionRequestDto {

    private Long materialId;
    private Double quantity;
    private String area;
    private String tower;
    private LocalDate consDate;
}
