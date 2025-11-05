package com.web.tilotoma.dto;
import lombok.Data;

import java.util.List;

@Data
public class LabourRequest {
    private Long contractorId;
    private String labourName;
    private String email;
    private String mobileNumber;
    private Long labourTypeId;
    private List<Long> projectIds;
}