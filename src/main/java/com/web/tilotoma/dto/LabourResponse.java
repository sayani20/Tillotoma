package com.web.tilotoma.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabourResponse {
    private Long id;
    private String labourName;
    private String labourUserId;
    private String email;
    private String mobileNumber;

    private Long contractorId;
    private String contractorName;
    private Double ratePerDay;
    private Double ratePerHour;

    private Long labourTypeId;
    private String labourTypeName;

    private String address;
    private String aadharNumber;

    private List<ProjectSimpleResponse> projects;

   /* private List<Long> projectIds;
    private List<String> projectNames;*/
}
