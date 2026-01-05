package com.web.tilotoma.controller;

import com.web.tilotoma.dto.MaterialRequestDto;
import com.web.tilotoma.dto.MaterialResponseDto;
import com.web.tilotoma.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    // ➕ Add material
    @PostMapping("addMaterial")
    public ResponseEntity<MaterialResponseDto> addMaterial(
            @RequestBody MaterialRequestDto request) {

        return ResponseEntity.ok(materialService.addMaterial(request));
    }

    // ✏️ Update material
    @PutMapping("updateMaterial/{id}")
    public ResponseEntity<MaterialResponseDto> updateMaterial(
            @PathVariable Long id,
            @RequestBody MaterialRequestDto request) {

        return ResponseEntity.ok(materialService.updateMaterial(id, request));
    }

    // ❌ Delete material (soft delete)
    @DeleteMapping("deleteMaterial/{id}")
    public ResponseEntity<String> deleteMaterial(@PathVariable Long id) {

        materialService.deleteMaterial(id);
        return ResponseEntity.ok("Material deleted successfully");
    }

    // 📄 Get all active materials
    @GetMapping("getAllMaterials")
    public ResponseEntity<List<MaterialResponseDto>> getAllMaterials() {

        return ResponseEntity.ok(materialService.getAllActiveMaterials());
    }
}
