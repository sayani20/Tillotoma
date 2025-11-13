package com.web.tilotoma.dto.response;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabourBillingDto {
    private Long labourId;
    private String labourName;
    private Double totalHours;
    private Long totalDays;
    private Double totalAmount;
}
