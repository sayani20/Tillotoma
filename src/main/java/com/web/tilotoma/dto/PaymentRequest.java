package com.web.tilotoma.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class PaymentRequest {
    private Long contractorId;
    private String billNo;
    private LocalDate billDate;
    private Double totalAmount;
    private Double paidAmount;
    private String remarks;
    private String paymentMethod;

}
