package com.web.tilotoma.service;

import com.web.tilotoma.dto.MaterialRequestDto;
import com.web.tilotoma.dto.MaterialResponseDto;

import java.util.List;

public interface MaterialService {

    MaterialResponseDto addMaterial(MaterialRequestDto request);

    MaterialResponseDto updateMaterial(Long materialId, MaterialRequestDto request);

    void deleteMaterial(Long materialId);

    List<MaterialResponseDto> getAllActiveMaterials();
}

