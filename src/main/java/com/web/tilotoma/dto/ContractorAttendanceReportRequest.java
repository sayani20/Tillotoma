package com.web.tilotoma.dto;

import java.time.LocalDate;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ContractorAttendanceReportRequest {
    private Long contractorId;
    private LocalDate date;
}
