package com.web.tilotoma.service;

import com.web.tilotoma.entity.material.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface OrderHistoryProjection {
    Long getOrderId();
    String getOrderNumber();
    LocalDate getOrderDate();
    LocalDate getRequiredBy();
    Long getNoOfItems();

    Double getTotalAmount();
    Double getPaidAmount();
    String getRemarks();

    OrderStatus getStatus();
    LocalDate getApprovedOn();
    String getVendorName();
    String getChallanNumber();
    LocalDateTime getReceivedOn();
    String getOrderReceivedType();
}
