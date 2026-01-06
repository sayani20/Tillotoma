package com.web.tilotoma.service;

import com.web.tilotoma.dto.VendorRequestDto;
import com.web.tilotoma.dto.VendorResponseDto;
import com.web.tilotoma.entity.material.Vendor;

import java.util.List;

public interface VendorService {
    public String addVendor(VendorRequestDto req);
    String updateVendor(VendorRequestDto req);
    String deleteVendor(Long id);
    List<Vendor> getAllActiveVendors();
}
