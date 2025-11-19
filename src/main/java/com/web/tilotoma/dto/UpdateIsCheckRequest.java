package com.web.tilotoma.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class UpdateIsCheckRequest {
    private Long labourId;
    private LocalDate attendanceDate;
    private Boolean isCheck;
}
