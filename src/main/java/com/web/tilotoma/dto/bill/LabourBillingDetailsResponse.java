package com.web.tilotoma.dto.bill;


import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabourBillingDetailsResponse {
    private Long labourId;
    private String labourName;
    private String labourType;
    private Long totalDays;
    private Double totalHours;
    private Double ratePerDay;
    private Double billAmount;
    private LocalDate attendanceDate;
}
