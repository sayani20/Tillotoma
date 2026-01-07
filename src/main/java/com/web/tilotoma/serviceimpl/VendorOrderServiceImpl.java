package com.web.tilotoma.serviceimpl;
import com.web.tilotoma.dto.VendorOrderDto;
import com.web.tilotoma.entity.material.*;
import com.web.tilotoma.repository.*;
import com.web.tilotoma.service.VendorOrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendorOrderServiceImpl implements VendorOrderService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private VendorOrderRepository orderRepository;

   /* @Override
    @Transactional   // 🔥 THIS IS THE FIX
    public String createOrder(VendorOrderDto.CreateOrderRequest request) {

        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        VendorOrder order = VendorOrder.builder()
                .vendor(vendor)
                .status(OrderStatus.PENDING)
                .orderDate(request.getOrderDate())
                .requiredBy(request.getRequiredBy())
                .remarks(request.getRemarks())
                .totalAmount(request.getTotalAmount())
                .build();

        List<VendorOrderItem> items = request.getItems().stream().map(i -> {

            Material material = materialRepository.findById(i.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Material not found"));

            return VendorOrderItem.builder()
                    .vendorOrder(order)
                    .material(material)
                    .brand(i.getBrand())
                    .unit(i.getUnit())
                    .quantity(i.getQuantity())
                    .rate(i.getRate())
                    .netAmount(i.getNetAmount())
                    .build();
        }).toList();

        order.setItems(items);

        // save 1 → generate ID
        VendorOrder saved = orderRepository.save(order);

        // derive order number
        saved.setOrderNumber("ODR-" + saved.getId());

        // save 2 → update order number
        orderRepository.save(saved);

        return "Vendor order created successfully. Order No: ODR-" + saved.getId();
    }*/


    @Override
    @Transactional
    public VendorOrderDto.CreateOrderResponse createOrder(
            VendorOrderDto.CreateOrderRequest request) {

        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        VendorOrder order = VendorOrder.builder()
                .vendor(vendor)
                .status(OrderStatus.PENDING)
                .orderDate(request.getOrderDate())
                .requiredBy(request.getRequiredBy())
                .remarks(request.getRemarks())
                .totalAmount(request.getTotalAmount())
                .build();

        List<VendorOrderItem> items = request.getItems().stream().map(i -> {

            Material material = materialRepository.findById(i.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Material not found"));

            return VendorOrderItem.builder()
                    .vendorOrder(order)
                    .material(material)
                    .brand(i.getBrand())
                    .unit(i.getUnit())
                    .quantity(i.getQuantity())
                    .rate(i.getRate())
                    .netAmount(i.getNetAmount())
                    .build();
        }).collect(java.util.stream.Collectors.toList()); // ✅ MUTABLE LIST

        order.setItems(items);

        VendorOrder saved = orderRepository.save(order);

        saved.setOrderNumber("ODR-" + saved.getId());

        orderRepository.save(saved);

        return new VendorOrderDto.CreateOrderResponse(
                saved.getId(),
                saved.getOrderNumber(),
                saved.getStatus()
        );
    }

    @Override
    public List<VendorOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public String updateOrderStatus(Long orderId, OrderStatus status) {

        VendorOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        orderRepository.save(order);

        return "Order status updated successfully";
    }
}