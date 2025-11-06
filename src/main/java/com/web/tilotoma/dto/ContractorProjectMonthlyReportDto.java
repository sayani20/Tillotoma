package com.web.tilotoma.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractorProjectMonthlyReportDto {
    private String contractorName;
    private String projectName;
    private String month;
    private Long totalLabour;
    private Long totalWorkingDays;
    private Double totalHours;
    private Double totalAmount;
}