package com.web.tilotoma.dto;

import lombok.Data;

@Data
public class LabourTypeRequest {
    private String typeName;
    private Double dailyRate;
    private Double hourlyRate;
}
