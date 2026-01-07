package com.web.tilotoma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MaterialReceiveResponseDto {

    private Long orderId;

    private Double totalReceivedAmount;

    private Double totalPaidAmount;

    private Double outstandingAmount;
}
