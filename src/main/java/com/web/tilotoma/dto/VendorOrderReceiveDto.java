package com.web.tilotoma.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class VendorOrderReceiveDto {
    // ✅ RECEIVE REQUEST
    @Data
    public static class ReceiveMaterialRequest {
        private Long orderId;
        private String challanNumber;
        private List<ReceiveItem> items;
    }

    @Data
    public static class ReceiveItem {
        private Long materialId;
        private Double receivedQuantity;
        private Double receivedRate;
    }

    // ✅ RESPONSE (LIST)
    @Data
    public static class ReceiveResponse {
        private Long receiveId;
        private Long materialId;
        private String materialName;
        private Double quantity;
        private Double rate;
        private Double amount;
        private String challanNumber;
        private LocalDateTime receivedOn;
    }
}
