package com.web.tilotoma.serviceimpl;
import com.web.tilotoma.dto.OrderHistoryResponseDto;
import com.web.tilotoma.dto.VendorOrderDto;
import com.web.tilotoma.dto.VendorOrderListResponseDto;
import com.web.tilotoma.entity.material.*;
import com.web.tilotoma.repository.*;
import com.web.tilotoma.service.VendorOrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VendorOrderServiceImpl implements VendorOrderService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private VendorOrderRepository orderRepository;

    @Override
    public List<OrderHistoryResponseDto> getOrderHistory(
            LocalDate fromDate,
            LocalDate toDate) {

        return orderRepository.findOrderHistory(
                OrderStatus.APPROVED,
                fromDate,
                toDate
        );
    }



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

/*    @Override
    public List<VendorOrder> getAllOrders() {
        return orderRepository.findAll();
    }*/


    @Override
    public List<VendorOrderListResponseDto> getAllOrders(
            LocalDate fromDate,
            LocalDate toDate) {

        List<VendorOrder> orders =
                orderRepository.findOrdersByDateRange(fromDate, toDate);

        List<VendorOrderListResponseDto> response = new ArrayList<>();

        for (VendorOrder order : orders) {

            VendorOrderListResponseDto dto =
                    new VendorOrderListResponseDto();

            dto.setOrderId(order.getId());
            dto.setOrderNumber(order.getOrderNumber());
            dto.setStatus(order.getStatus());
            dto.setRemarks(order.getRemarks());
            dto.setOrderDate(order.getOrderDate());
            dto.setRequiredBy(order.getRequiredBy());
            dto.setTotalAmount(order.getTotalAmount());

            dto.setVendorId(order.getVendor().getId());
            dto.setVendorName(order.getVendor().getVendorName());

            List<VendorOrderListResponseDto.OrderItemDto> itemDtos =
                    new ArrayList<>();

            for (VendorOrderItem item : order.getItems()) {
                VendorOrderListResponseDto.OrderItemDto i =
                        new VendorOrderListResponseDto.OrderItemDto();

                i.setMaterialId(item.getMaterial().getId());
                i.setMaterialName(item.getMaterial().getMaterialName());
                i.setUnit(item.getUnit());
                i.setQuantity(item.getQuantity());
                i.setRate(item.getRate());
                i.setBrand(item.getBrand());
                i.setNetAmount(item.getNetAmount());

                itemDtos.add(i);
            }

            dto.setItems(itemDtos);
            response.add(dto);
        }

        return response;
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