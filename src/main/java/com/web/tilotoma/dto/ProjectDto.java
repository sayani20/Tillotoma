package com.web.tilotoma.dto;

import com.web.tilotoma.entity.ProjectType;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ProjectDto {
    private String name;
    private String description;
    private String location;
    private String city;
    private String state;
    private String pincode;
    private Double landArea;
    private Double builtUpArea;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime possessionDate;
    private ProjectType projectType;
    private String projectStatus;
    private Integer totalNumberOfFlats;
    private Integer totalNumberOfFloors;
    private Double budgetEstimate;
    private String remarks;
    private Boolean approvedByAuthority;
    private String approvalNumber;
    private Long createdBy;
    private Long contractorId;
}
