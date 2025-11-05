package com.web.tilotoma.dto.response;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabourResponseDto {
    private Long id;
    private String labourName;
    private String email;
    private String mobileNumber;
    private Boolean isActive;
    private LocalDateTime createdOn;

    private Long contractorId;
    private String contractorName;

    private Long labourTypeId;
    private String labourTypeName;

    private List<ProjectDto> projects;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectDto {
        private Long id;
        private String name;
    }
}
