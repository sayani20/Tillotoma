package com.web.tilotoma.controller;

import com.web.tilotoma.dto.*;
import com.web.tilotoma.entity.material.VendorOrderReceive;
import com.web.tilotoma.service.VendorOrderReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor-order-receive")
public class VendorOrderReceiveController {

    @Autowired
    private VendorOrderReceiveService receiveService;

    // ✅ RECEIVE MATERIAL
    @PostMapping("/receive")
    public ResponseEntity<ApiResponse<MaterialReceiveResponseDto>>
    receiveMaterial(@RequestBody MaterialReceiveRequestDto request) {

        try {
            MaterialReceiveResponseDto response =
                    receiveService.receiveMaterial(request);

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Material received successfully",
                            response
                    )
            );

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(
                            false,
                            "Failed to receive material",
                            null
                    ));
        }
    }

    // ✅ GET RECEIVE LIST BY ORDER
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<VendorOrderReceiveDto.ReceiveResponse>>>
    getReceivedList(@PathVariable Long orderId) {

        try {
            List<VendorOrderReceiveDto.ReceiveResponse> list =
                    receiveService.getReceivedByOrder(orderId);

            return ResponseEntity.ok(
                    new ApiResponse<>(true,
                            "Received materials fetched successfully",
                            list)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false,
                            "Failed to fetch received materials",
                            null));
        }
    }
    @GetMapping("/materialDetails/{materialId}")
    public ResponseEntity<List<Map<String, Object>>> getMaterialStock(
            @PathVariable Long materialId) {

        return ResponseEntity.ok(
                receiveService.getMaterialStockById(materialId)
        );
    }


    @GetMapping("/receivedOrderList")
    public ResponseEntity<ApiResponse<List<VendorOrderReceiveResponseDto>>>
    getReceivedOrders(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate
    ) {

        List<VendorOrderReceiveResponseDto> list =
                receiveService.getReceivedOrders(fromDate, toDate);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Received orders fetched successfully",
                        list
                )
        );
    }


    @GetMapping("/receivedList")
    public List<ReceiveResponseDto> getByMaterialAndDate(
            @RequestParam Long materialId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate
    ) {
        return receiveService.getReceives(materialId, fromDate, toDate);
    }
}
