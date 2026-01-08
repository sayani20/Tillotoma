package com.web.tilotoma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VendorOrderReceiveResponseDto {

    private Long receiveId;

    private Long orderId;
    private String orderNumber;

    private Long materialId;
    private String materialName;
    private String unit;

    private Double receivedQuantity;
    private Double receivedRate;
    private Double receivedAmount;

    private String challanNumber;
    private LocalDateTime receivedOn;
}
