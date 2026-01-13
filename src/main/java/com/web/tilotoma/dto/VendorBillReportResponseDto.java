package com.web.tilotoma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorBillReportResponseDto {

    private Long vendorId;
    private String vendorName;

    private Long orderId;
    private String orderNumber;
    private String challanNumber;

    private Double totalAmount;     // order amount (received)
    private Double paidAmount;
    private String paymentMode;
    private Double regularBalance;  // paid - orderAmount
    private Double finalBalance;    // running vendor balance

    private LocalDate orderDate;
}
