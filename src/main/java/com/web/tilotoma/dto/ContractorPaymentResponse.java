package com.web.tilotoma.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractorPaymentResponse {
    private Long contractorId;
    private String contractorName;

    private String billNo;
    private LocalDate billDate;

    private Double totalAmount;
    private Double paidAmount;

    private String status;
    private LocalDate paymentDate;

    private String remarks;

    private List<ContractorPaymentHistoryResponse> paymentHistories;
}
