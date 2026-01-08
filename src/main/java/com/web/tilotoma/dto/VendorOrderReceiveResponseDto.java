package com.web.tilotoma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorOrderReceiveResponseDto {

  /*  private Long receiveId;

    private Long orderId;
    private String orderNumber;

    private Long materialId;
    private String materialName;
    private String unit;

    private Double receivedQuantity;
    private Double receivedRate;
    private Double receivedAmount;

    private String challanNumber;
    private LocalDateTime receivedOn;*/

    private Long receiveId;
    private Long orderId;
    private String challanNumber;
    private String vendorName;
    private Date receivedDate;
    private Long noOfItems;

    // getters
    public Long getReceiveId() { return receiveId; }
    public Long getOrderId() { return orderId; }
    public String getChallanNumber() { return challanNumber; }
    public String getVendorName() { return vendorName; }
    public Date getReceivedDate() { return receivedDate; }
    public Long getNoOfItems() { return noOfItems; }
}
