package com.web.tilotoma.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateCustomAmountRequest {
    private Long labourId;
    private LocalDate attendanceDate;
    private Double customAmount;
}

