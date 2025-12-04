package com.web.tilotoma.dto.bill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private double totalHours;
    private double totalAmount;

    private Long projectId;
    private String projectName;
    private int totalLabours;

    // 🔥 New fields
    private String billNo;
    private LocalDate billDate;

    private Double paidAmount;
    private String billStatus;

}
