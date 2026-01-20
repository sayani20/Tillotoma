package com.web.tilotoma.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ConsumptionRequestDto {
    private Long consumptionId;

    private Long materialId;
    private Double quantity;

    private String area;    // Flat / Floor / Area
    private String tower;   // Tower / Block

    private LocalDateTime consDate; // optional
}
