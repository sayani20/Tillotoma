package com.web.tilotoma.service;

import com.web.tilotoma.dto.VendorRequestDto;
import com.web.tilotoma.dto.VendorResponseDto;

import java.util.List;

public interface VendorService {
    VendorResponseDto addVendor(VendorRequestDto dto);
    VendorResponseDto updateVendor(Long id, VendorRequestDto dto);
    void deleteVendor(Long id);
    List<VendorResponseDto> getAllVendors();
}
