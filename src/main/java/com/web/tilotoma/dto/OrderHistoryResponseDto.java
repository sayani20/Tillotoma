package com.web.tilotoma.dto;

import com.web.tilotoma.entity.material.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryResponseDto {

    private Long orderId;
    private String orderNumber;
    private LocalDate orderDate;
    private LocalDate requiredBy;
    private Long noOfItems;
    private Double totalAmount;
    private String remarks;
    private OrderStatus status;
    private LocalDate approvedOn;
}
