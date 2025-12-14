package com.web.tilotoma.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractorPaymentHistoryResponse {
    private Double paidAmount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String remarks;
}
