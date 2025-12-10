package com.web.tilotoma.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractorAttendanceReportDto {
    private Long labourId;
    private String labourName;
    private String labourUserId;
    private Double ratePerDay;
    private LabourTypeDto labourType;
    private List<ProjectDto> projects;

    private AttendanceDto attendance;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LabourTypeDto {
        private Long id;
        private String typeName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProjectDto {
        private Long id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AttendanceDto {
        private LocalDate attendanceDate;
        private LocalTime inTime;
        private LocalTime outTime;
        private Boolean isPresent;
        private Boolean isCheck;
        private Long durationMinutes;
        private Double todayBillAmount;
        private Double calculatedAmount;
        private Double customAmount;


    }
}
