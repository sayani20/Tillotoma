package com.web.tilotoma.serviceimpl;

import com.web.tilotoma.dto.VendorRequestDto;
import com.web.tilotoma.dto.VendorResponseDto;
import com.web.tilotoma.entity.material.Vendor;
import com.web.tilotoma.repository.VendorRepository;
import com.web.tilotoma.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {
    @Autowired
    private VendorRepository vendorRepository;
    @Override
    public VendorResponseDto addVendor(VendorRequestDto dto) {
        Vendor vendor = Vendor.builder()
                .vendorName(dto.getVendorName())
                .contactPersonName(dto.getContactPersonName())
                .mobile(dto.getMobile())
                .emailId(dto.getEmailId())
                .materialCategory(dto.getMaterialCategory())
                .gstNumber(dto.getGstNumber())
                .address(dto.getAddress())
                .isActive(true)
                .build();

        Vendor saved = vendorRepository.save(vendor);

        return VendorResponseDto.builder()
                .id(saved.getId())
                .vendorName(saved.getVendorName())
                .contactPersonName(saved.getContactPersonName())
                .mobile(saved.getMobile())
                .emailId(saved.getEmailId())
                .gstNumber(saved.getGstNumber())
                .address(saved.getAddress())
                .isActive(saved.getIsActive())
                .build();

    }

    public VendorResponseDto updateVendor(Long id, VendorRequestDto dto) {

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        vendor.setVendorName(dto.getVendorName());
        vendor.setContactPersonName(dto.getContactPersonName());
        vendor.setMobile(dto.getMobile());
        vendor.setEmailId(dto.getEmailId());
        vendor.setMaterialCategory(dto.getMaterialCategory());
        vendor.setGstNumber(dto.getGstNumber());
        vendor.setAddress(dto.getAddress());

        Vendor updated = vendorRepository.save(vendor);

        return VendorResponseDto.builder()
                .id(updated.getId())
                .vendorName(updated.getVendorName())
                .contactPersonName(updated.getContactPersonName())
                .mobile(updated.getMobile())
                .emailId(updated.getEmailId())
                .materialCategory(updated.getMaterialCategory())
                .gstNumber(updated.getGstNumber())
                .address(updated.getAddress())
                .isActive(updated.getIsActive())
                .build();
    }
    public void deleteVendor(Long id) {

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        vendor.setIsActive(false);
        vendorRepository.save(vendor);
    }

    public List<VendorResponseDto> getAllVendors() {

        List<Vendor> vendors = vendorRepository.findByIsActiveTrue();
        List<VendorResponseDto> responseList = new ArrayList<>();

        for (Vendor v : vendors) {
            responseList.add(
                    VendorResponseDto.builder()
                            .id(v.getId())
                            .vendorName(v.getVendorName())
                            .contactPersonName(v.getContactPersonName())
                            .mobile(v.getMobile())
                            .emailId(v.getEmailId())
                            .materialCategory(v.getMaterialCategory())
                            .gstNumber(v.getGstNumber())
                            .address(v.getAddress())
                            .isActive(v.getIsActive())
                            .build()
            );
        }

        return responseList;
    }


}
