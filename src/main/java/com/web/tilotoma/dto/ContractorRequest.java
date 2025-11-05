package com.web.tilotoma.dto;

import lombok.Data;

import java.util.List;

@Data
public class ContractorRequest {
    private String contractorName;
    private String userName;
    private String email;
    private String mobileNumber;
    private String address;
    //private Long projectId;
    private List<Long> projectIds;
}

