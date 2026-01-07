package com.web.tilotoma.service;

import com.web.tilotoma.dto.VendorOrderDto;
import com.web.tilotoma.entity.material.OrderStatus;
import com.web.tilotoma.entity.material.VendorOrder;

import java.util.List;

public interface  VendorOrderService {
   // String createOrder(VendorOrderDto.CreateOrderRequest request);

    List<VendorOrder> getAllOrders();

    String updateOrderStatus(Long orderId, OrderStatus status);

    VendorOrderDto.CreateOrderResponse createOrder(
            VendorOrderDto.CreateOrderRequest request
    );
}
