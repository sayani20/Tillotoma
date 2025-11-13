package com.web.tilotoma.dto.response;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectBillingDto {
    private Long projectId;
    private String projectName;
    private Double totalHours;
    private Double totalAmount;
    private List<LabourBillingDto> labours;
}
