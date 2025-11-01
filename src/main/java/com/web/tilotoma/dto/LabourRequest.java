package com.web.tilotoma.dto;
import lombok.Data;

@Data
public class LabourRequest {
    private Long contractorId;
    private String labourName;
    private String email;
    private String mobileNumber;
    private Long labourTypeId;
}