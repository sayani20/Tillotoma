package com.web.tilotoma.dto;

import com.web.tilotoma.entity.material.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class VendorOrderDto {

    // ================= CREATE ORDER REQUEST =================
    @Data
    public static class CreateOrderRequest {
        private Long vendorId;
        private LocalDate orderDate;
        private LocalDate requiredBy;
        private String remarks;
        private Double totalAmount;
        private List<OrderItem> items;
    }

    // ================= ORDER ITEM REQUEST =================
    @Data
    public static class OrderItem {
        private Long materialId;
        private String brand;
        private String unit;
        private Double quantity;
        private Double rate;
        private Double netAmount;
    }

    // ================= STATUS UPDATE REQUEST =================
    @Data
    public static class StatusUpdateRequest {
        private OrderStatus status;
    }

    // ================= CREATE ORDER RESPONSE =================
    @Data
    @AllArgsConstructor
    public static class CreateOrderResponse {
        private Long orderId;
        private String orderNumber;
        private OrderStatus status;
    }

    // ================= GET ALL ORDERS RESPONSE =================
    @Data
    @AllArgsConstructor
    public static class OrderListResponse {
        private Long orderId;
        private String orderNumber;
        private String vendorName;
        private LocalDate orderDate;
        private Double totalAmount;
        private OrderStatus status;
    }
}
