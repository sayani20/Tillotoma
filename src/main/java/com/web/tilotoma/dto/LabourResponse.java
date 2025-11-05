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
    private String email;
    private String mobileNumber;

    private Long contractorId;
    private String contractorName;

    private Long labourTypeId;
    private String labourTypeName;

   /* private List<Long> projectIds;
    private List<String> projectNames;*/
}
