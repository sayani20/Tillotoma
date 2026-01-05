package com.web.tilotoma.serviceimpl;

import com.web.tilotoma.dto.MaterialRequestDto;
import com.web.tilotoma.dto.MaterialResponseDto;
import com.web.tilotoma.entity.material.Material;
import com.web.tilotoma.repository.MaterialRepository;
import com.web.tilotoma.service.MaterialService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    @Override
    public MaterialResponseDto addMaterial(MaterialRequestDto request) {

        if (materialRepository.existsByMaterialNameIgnoreCase(request.getMaterialName())) {
            throw new RuntimeException("Material already exists");
        }

        Material material = Material.builder()
                .materialName(request.getMaterialName())
                .unit(request.getUnit())
                .category(request.getCategory())
                .minimumLimit(request.getMinimumLimit())
                .isActive(true)
                .build();

        materialRepository.save(material);
        return mapToDto(material);
    }

    @Override
    public MaterialResponseDto updateMaterial(Long materialId, MaterialRequestDto request) {

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material not found"));

        if (request.getMaterialName() != null) {
            material.setMaterialName(request.getMaterialName());
        }
        if (request.getUnit() != null) {
            material.setUnit(request.getUnit());
        }
        if (request.getCategory() != null) {
            material.setCategory(request.getCategory());
        }
        if (request.getMinimumLimit() != null) {
            material.setMinimumLimit(request.getMinimumLimit());
        }

        materialRepository.save(material);
        return mapToDto(material);
    }

    @Override
    public void deleteMaterial(Long materialId) {

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material not found"));

        material.setIsActive(false); // ✅ Soft delete
        materialRepository.save(material);
    }

    @Override
    public List<MaterialResponseDto> getAllActiveMaterials() {

        return materialRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private MaterialResponseDto mapToDto(Material material) {

        return new MaterialResponseDto(
                material.getId(),
                material.getMaterialName(),
                material.getUnit(),
                material.getCategory(),
                material.getMinimumLimit(),
                material.getIsActive()
        );
    }
}
