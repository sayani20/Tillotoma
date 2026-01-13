package com.web.tilotoma.controller;


import com.web.tilotoma.dto.ApiResponse;
import com.web.tilotoma.dto.VendorOrderItemUpdateRequest;
import com.web.tilotoma.dto.VendorOrderItemUpdateResponse;
import com.web.tilotoma.dto.VendorRequestDto;
import com.web.tilotoma.entity.material.Vendor;
import com.web.tilotoma.service.VendorOrderService;
import com.web.tilotoma.service.VendorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @Autowired
    private VendorOrderService vendorOrderService;

    // ➕ Add Vendor
    @PostMapping("/addVendor")
    public ResponseEntity<ApiResponse<String>> addVendor(
            @Valid @RequestBody VendorRequestDto requestDto) {

        try {
            String msg = vendorService.addVendor(requestDto);
            return ResponseEntity.ok(new ApiResponse<>(true, msg, null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // ✏️ Update Vendor
    @PostMapping("/updateVendor")
    public ResponseEntity<ApiResponse<String>> updateVendor(
            @Valid @RequestBody VendorRequestDto requestDto) {

        try {
            String msg = vendorService.updateVendor(requestDto);
            return ResponseEntity.ok(new ApiResponse<>(true, msg, null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // ❌ Delete Vendor
    @DeleteMapping("/deleteVendor/{id}")
    public ResponseEntity<ApiResponse<String>> deleteVendor(@PathVariable Long id) {
        try {
            String msg = vendorService.deleteVendor(id);
            return ResponseEntity.ok(new ApiResponse<>(true, msg, null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // 📥 Get All Active Vendors
    @GetMapping("/allVendor")
    public ResponseEntity<ApiResponse<List<Vendor>>> allActive() {

        List<Vendor> list = vendorService.getAllActiveVendors();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Active vendors fetched successfully", list)
        );
    }

    @PutMapping("/update-order-items")
    public ResponseEntity<ApiResponse<VendorOrderItemUpdateResponse>> updateOrderItems(
            @RequestBody VendorOrderItemUpdateRequest request) {

        VendorOrderItemUpdateResponse response =
                vendorOrderService.updateOrderItems(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Order updated successfully", response)
        );
    }

}
