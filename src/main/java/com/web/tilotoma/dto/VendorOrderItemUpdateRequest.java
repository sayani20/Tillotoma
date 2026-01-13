package com.web.tilotoma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorOrderItemUpdateRequest {

    private Long vendorId;
    private Long orderId;
    private List<Item> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private Long orderItemId;
        private Double quantity;
        private Double rate;
        private String brand;
    }
}

