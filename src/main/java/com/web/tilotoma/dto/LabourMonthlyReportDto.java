package com.web.tilotoma.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LabourMonthlyReportDto {
    private String labourName;
    private String labourType;
    private long daysWorked;
    private double totalHours;
    private double dailyRate;
    private double totalAmount;
    private Long contractorId;
}