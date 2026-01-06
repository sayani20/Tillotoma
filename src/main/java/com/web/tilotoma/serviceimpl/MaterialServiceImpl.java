package com.web.tilotoma.serviceimpl;

import com.web.tilotoma.dto.MaterialRequestDto;
import com.web.tilotoma.dto.MaterialResponseDto;
import com.web.tilotoma.entity.material.Material;
import com.web.tilotoma.entity.material.MaterialCategory;
import com.web.tilotoma.repository.CategoryRepository;
import com.web.tilotoma.repository.MaterialRepository;
import com.web.tilotoma.service.MaterialService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaterialServiceImpl implements MaterialService {
    @Autowired

    private  MaterialRepository materialRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public String addMaterial(MaterialRequestDto request) {

        String catName = request.getMaterialCategory();

        if (catName == null || catName.trim().isEmpty()) {
            throw new RuntimeException("Category name required");
        }

        MaterialCategory cat = categoryRepository
                .findByName(catName.trim())
                .orElse(null);

        if (cat == null) {
            cat = MaterialCategory.builder()
                    .name(catName.trim())
                    .isActive(true)
                    .createdOn(LocalDateTime.now())
                    .build();

            categoryRepository.save(cat);
        }

        Material material = Material.builder()
                .materialName(request.getMaterialName())
                .unit(request.getUnit())
                .minimumLimit(request.getMinimumLimit())
                .brand(request.getBrand())
                .materialCategory(cat)
                .createdOn(LocalDateTime.now())
                .isActive(true)
                .build();

        materialRepository.save(material);

        return "Material saved successfully";
    }

    @Override
    public String updateMaterial(MaterialRequestDto req) {
        // --- Validation ---
        if (req.getId() == null) {
            throw new RuntimeException("Material id required");
        }

        String catName = req.getMaterialCategory();
        if (catName == null || catName.trim().isEmpty()) {
            throw new RuntimeException("Category name required");
        }

        // --- Existing Material Fetch ---
        Material material = materialRepository.findById(req.getId())
                .orElseThrow(() -> new RuntimeException("Material not found"));

        // --- Category check + auto create ---
        MaterialCategory cat = categoryRepository.findByName(catName.trim())
                .orElse(null);

        if (cat == null) {
            cat = MaterialCategory.builder()
                    .name(catName.trim())
                    .isActive(true)
                    .createdOn(LocalDateTime.now())
                    .build();

            categoryRepository.save(cat);
        }

        // --- Field Update ---
        material.setMaterialName(req.getMaterialName());
        material.setUnit(req.getUnit());
        material.setBrand(req.getBrand());
        material.setMinimumLimit(req.getMinimumLimit());
        material.setMaterialCategory(cat);
        material.setCreatedOn(LocalDateTime.now());

        materialRepository.save(material);

        return "Material updated successfully";
    }
    public String deleteMaterial(Long id) {

        if (id == null) {
            throw new RuntimeException("Material id required");
        }

        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found"));

        // 🔥 Soft delete
        material.setIsActive(false);
        materialRepository.save(material);

        return "Material soft deleted successfully";
    }
    public List<Material> getAllMaterials() {
        return materialRepository.findByIsActiveTrue();
    }

    public List<Material> getMaterialByCategory(Long categoryId) {

        if (categoryId == null) {
            throw new RuntimeException("Category id required");
        }

        return materialRepository
                .findByMaterialCategory_IdAndIsActiveTrue(categoryId);
    }




}
