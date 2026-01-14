package com.web.tilotoma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class VendorBillReportResponseDto {

    private Long vendorId;
    private String vendorName;

    private Long orderId;
    private String orderNumber;

    private String challanNumber;

    private Double totalAmount;     // received amount
    private Double paidAmount;

    private String paymentMode;     // CASH / UPI / BANK / MULTIPLE

    private Double regularBalance;  // paid - total
    private Double finalBalance;    // running vendor balance

    private LocalDate orderDate;
}
