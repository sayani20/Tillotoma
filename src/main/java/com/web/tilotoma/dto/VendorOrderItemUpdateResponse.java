package com.web.tilotoma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorOrderItemUpdateResponse {

    private Long orderId;
    private Boolean edited;
    private Double totalAmount;
    private String message;
}

