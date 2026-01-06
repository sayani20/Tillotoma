package com.web.tilotoma.serviceimpl;

import com.web.tilotoma.dto.VendorRequestDto;
import com.web.tilotoma.dto.VendorResponseDto;
import com.web.tilotoma.entity.material.Material;
import com.web.tilotoma.entity.material.MaterialCategory;
import com.web.tilotoma.entity.material.Vendor;
import com.web.tilotoma.repository.CategoryRepository;
import com.web.tilotoma.repository.MaterialRepository;
import com.web.tilotoma.repository.VendorRepository;
import com.web.tilotoma.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {
    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    public String addVendor(VendorRequestDto req) {

        // --- Validation ---
        if (req.getVendorName() == null
                || req.getVendorName().trim().isEmpty()) {
            throw new RuntimeException("Vendor name required");
        }

        if (req.getMaterialCategoryId() == null) {
            throw new RuntimeException("Category id required");
        }

        // --- Category fetch ---
        MaterialCategory cat = categoryRepository.findById(req.getMaterialCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // --- Materials integrity check ---
        List<Material> categoryWiseMaterials =
                materialRepository.findByMaterialCategory_IdAndIsActiveTrue(cat.getId());

        // selected materials ওই list এর ভিতরে আছে কি না
        List<Long> validMaterialIds =
                categoryWiseMaterials.stream()
                        .map(Material::getId)
                        .toList();

        for (Long mid : req.getMaterialIds()) {
            if (!validMaterialIds.contains(mid)) {
                throw new RuntimeException(
                        "Invalid material selected for this category"
                );
            }
        }

        // --- Vendor save ---
        Vendor vendor = Vendor.builder()
                .vendorName(req.getVendorName().trim())
                .contactPersonName(req.getContactPersonName())
                .mobile(req.getMobile())
                .emailId(req.getEmailId())
                .materialCategory(cat)          // ← FK
                .gstNumber(req.getGstNumber())
                .address(req.getAddress())
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .build();

        // materials set
        List<Material> selected =
                materialRepository.findAllById(req.getMaterialIds());

        vendor.setMaterials(selected);

        vendorRepository.save(vendor);

        return "Vendor saved successfully";
    }

    public String updateVendor(VendorRequestDto req) {

        // --- Validation ---
        if (req.getId() == null) {
            throw new RuntimeException("Vendor id required");
        }

        if (req.getMaterialCategoryId() == null) {
            throw new RuntimeException("Category id required");
        }

        // --- Existing Vendor Fetch ---
        Vendor vendor = vendorRepository.findById(req.getId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        // --- Category Fetch ---
        MaterialCategory cat = categoryRepository.findById(req.getMaterialCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // --- Integrity Check ---
        List<Material> categoryWise =
                materialRepository.findByMaterialCategory_IdAndIsActiveTrue(cat.getId());

        List<Long> validIds =
                categoryWise.stream().map(Material::getId).toList();

        for (Long mid : req.getMaterialIds()) {
            if (!validIds.contains(mid)) {
                throw new RuntimeException("Invalid material for this category");
            }
        }

        // --- Field Update ---
        vendor.setVendorName(req.getVendorName());
        vendor.setContactPersonName(req.getContactPersonName());
        vendor.setMobile(req.getMobile());
        vendor.setEmailId(req.getEmailId());
        vendor.setAddress(req.getAddress());
        vendor.setGstNumber(req.getGstNumber());

        // category relation
        vendor.setMaterialCategory(cat);

        // materials set
        List<Material> selected =
                materialRepository.findAllById(req.getMaterialIds());

        vendor.setMaterials(selected);

        vendorRepository.save(vendor);

        return "Vendor updated successfully";
    }

    public String deleteVendor(Long id) {

        if (id == null) {
            throw new RuntimeException("Vendor id required");
        }

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        // 🔥 soft delete
        vendor.setIsActive(false);
        vendorRepository.save(vendor);

        return "Vendor soft deleted successfully";
    }

    public List<Vendor> getAllActiveVendors() {
        return vendorRepository.findByIsActiveTrue();
    }


}
