package com.web.tilotoma.controller;


import com.web.tilotoma.dto.VendorRequestDto;
import com.web.tilotoma.dto.VendorResponseDto;
import com.web.tilotoma.service.VendorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorController {
    @Autowired
    private VendorService vendorService;

    @PostMapping("/addVendor")
    public ResponseEntity<VendorResponseDto> addVendor(
            @Valid @RequestBody VendorRequestDto requestDto) {

        return ResponseEntity.ok(vendorService.addVendor(requestDto));
    }
    @PutMapping("/updateVendor/{id}")
    public ResponseEntity<VendorResponseDto> updateVendor(
            @PathVariable Long id,
            @Valid @RequestBody VendorRequestDto dto) {

        return ResponseEntity.ok(vendorService.updateVendor(id, dto));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVendor(@PathVariable Long id) {

        vendorService.deleteVendor(id);
        return ResponseEntity.ok("Vendor deactivated successfully");
    }

    @GetMapping("/allVendors")
    public ResponseEntity<List<VendorResponseDto>> getAllVendors() {

        return ResponseEntity.ok(vendorService.getAllVendors());
    }



}
