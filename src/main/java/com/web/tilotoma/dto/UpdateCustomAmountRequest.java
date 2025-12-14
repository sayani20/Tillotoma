package com.web.tilotoma.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UpdateCustomAmountRequest {
    private Long labourId;
    private LocalDate attendanceDate;
    private Double customAmount;
    private String paymentMethod;
    private String remarks;
}

