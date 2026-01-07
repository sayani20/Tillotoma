package com.web.tilotoma.controller;
import com.web.tilotoma.dto.ApiResponse;
import com.web.tilotoma.dto.VendorOrderDto;
import com.web.tilotoma.dto.VendorOrderListResponseDto;
import com.web.tilotoma.entity.material.VendorOrder;
import com.web.tilotoma.service.VendorOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vendor-orders")
public class VendorOrderController {
    @Autowired
    private VendorOrderService orderService;



    // =====================================================
    // ✅ CREATE ORDER
    // =====================================================
    @PostMapping("/createOrder")
    public ResponseEntity<ApiResponse<VendorOrderDto.CreateOrderResponse>> createOrder(
            @RequestBody VendorOrderDto.CreateOrderRequest request) {

        VendorOrderDto.CreateOrderResponse response =
                orderService.createOrder(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Vendor order created successfully",
                        response
                )
        );
    }

    // ✅ Get All Vendor Orders
    @GetMapping("/allOrder")
    public ResponseEntity<ApiResponse<List<VendorOrderListResponseDto>>> getAllOrders(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate
    ) {
        try {
            List<VendorOrderListResponseDto> orders =
                    orderService.getAllOrders(fromDate, toDate);

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Vendor orders fetched successfully",
                            orders
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(
                            false,
                            "Failed to fetch vendor orders",
                            null
                    ));
        }
    }

    // ✅ Update Order Status (PENDING → APPROVED)
    @PutMapping("/{orderId}/orderStatus")
    public ResponseEntity<ApiResponse<String>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody VendorOrderDto.StatusUpdateRequest request) {

        try {
            String message = orderService.updateOrderStatus(orderId, request.getStatus());
            return ResponseEntity.ok(
                    new ApiResponse<>(true, message, null)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to update order status", null));
        }
    }
}
