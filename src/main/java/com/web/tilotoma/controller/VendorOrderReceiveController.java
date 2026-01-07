package com.web.tilotoma.controller;

import com.web.tilotoma.dto.ApiResponse;
import com.web.tilotoma.dto.MaterialReceiveRequestDto;
import com.web.tilotoma.dto.MaterialReceiveResponseDto;
import com.web.tilotoma.dto.VendorOrderReceiveDto;
import com.web.tilotoma.service.VendorOrderReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
