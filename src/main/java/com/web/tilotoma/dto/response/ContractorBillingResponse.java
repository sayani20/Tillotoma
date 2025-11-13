package com.web.tilotoma.dto.response;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractorBillingResponse {
    private Long contractorId;
    private String contractorName;
    private String contractorEmail;
    private String contractorMobile;
    private Double totalHours;
    private Double totalAmount;
    private List<ProjectBillingDto> projects;
}
