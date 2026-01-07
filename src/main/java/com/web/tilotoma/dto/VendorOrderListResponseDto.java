package com.web.tilotoma.dto;

import com.web.tilotoma.entity.material.OrderStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class VendorOrderListResponseDto {

    private Long orderId;
    private String orderNumber;

    private Long vendorId;
    private String vendorName;

    private OrderStatus status;
    private LocalDate orderDate;
    private LocalDate requiredBy;

    private Double totalAmount;

    private List<OrderItemDto> items;

    @Data
    public static class OrderItemDto {
        private Long materialId;
        private String materialName;
        private String unit;
        private Double quantity;
        private Double rate;
        private Double netAmount;
    }
}
