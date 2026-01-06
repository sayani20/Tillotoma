package com.web.tilotoma.controller;


import com.web.tilotoma.dto.VendorRequestDto;
import com.web.tilotoma.entity.material.Vendor;
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
    public ResponseEntity<String> addVendor(
            @Valid @RequestBody VendorRequestDto requestDto) {

        return ResponseEntity.ok(vendorService.addVendor(requestDto));
    }

    @PostMapping("/updateVendor")
    public ResponseEntity<String> updateVendor(
            @Valid @RequestBody VendorRequestDto requestDto) {

        String result = vendorService.updateVendor(requestDto);
        return ResponseEntity.ok(result);
    }
    @DeleteMapping ("/deleteVendor/{id}")
    public ResponseEntity<String> deleteVendor(
            @PathVariable Long id) {

        String result = vendorService.deleteVendor(id);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/allVendor")
    public ResponseEntity<List<Vendor>> allActive() {

        List<Vendor> list = vendorService.getAllActiveVendors();
        return ResponseEntity.ok(list);
    }






}
