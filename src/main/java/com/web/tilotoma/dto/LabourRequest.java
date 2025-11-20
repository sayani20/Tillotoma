package com.web.tilotoma.dto;
import lombok.Data;

import java.util.List;

@Data
public class LabourRequest {
    private Long id;
    private Long contractorId;
    private String labourName;
    private String email;
    private String mobileNumber;
    private Long labourTypeId;
    private Double ratePerDay;

    private String address;
    private String aadharNumber;

    private List<Long> projectIds;
}