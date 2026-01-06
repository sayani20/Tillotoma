package com.web.tilotoma.service;

import com.web.tilotoma.dto.MaterialRequestDto;
import com.web.tilotoma.dto.MaterialResponseDto;
import com.web.tilotoma.entity.material.Material;

import java.util.List;

public interface MaterialService {

    String addMaterial(MaterialRequestDto request);
    String updateMaterial(MaterialRequestDto req);
    String deleteMaterial(Long id);
    List<Material> getAllMaterials();
    List<Material> getMaterialByCategory(Long categoryId);


}

