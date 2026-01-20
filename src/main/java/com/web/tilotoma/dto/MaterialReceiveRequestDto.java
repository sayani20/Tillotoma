package com.web.tilotoma.dto;

import lombok.Data;

import java.util.List;

@Data
public class MaterialReceiveRequestDto {

    private Long orderId;
    private String challanNumber;

    // 🔹 optional payment at receive time
    private Double paidAmount;
    private String paymentMode;
    private String paymentRef;

    private List<Item> items;

    @Data
    public static class Item {
        private Long materialId;
        private Double quantity;
        private Double rate;
    }
    private String orderReceivedType;

}
