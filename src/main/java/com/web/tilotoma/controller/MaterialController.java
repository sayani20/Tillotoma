package com.web.tilotoma.controller;

import com.web.tilotoma.dto.ApiResponse;
import com.web.tilotoma.dto.MaterialRequestDto;
import com.web.tilotoma.dto.MaterialResponseDto;
import com.web.tilotoma.entity.material.Material;
import com.web.tilotoma.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    // ➕ Add Material
    @PostMapping("/addMaterial")
    public ResponseEntity<ApiResponse<String>> add(@RequestBody MaterialRequestDto req) {
        try {
            String msg = materialService.addMaterial(req);
            return ResponseEntity.ok(new ApiResponse<>(true, msg, null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // ✏️ Update Material
    @PutMapping("/updateMaterial")
    public ResponseEntity<ApiResponse<String>> update(@RequestBody MaterialRequestDto req) {
        try {
            String msg = materialService.updateMaterial(req);
            return ResponseEntity.ok(new ApiResponse<>(true, msg, null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // ❌ Soft Delete
    @DeleteMapping("/deleteMaterial/{id}")
    public ResponseEntity<ApiResponse<String>> softDelete(@PathVariable Long id) {
        try {
            String msg = materialService.deleteMaterial(id);
            return ResponseEntity.ok(new ApiResponse<>(true, msg, null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // 📥 All Materials
    @GetMapping("/allMaterials")
    public ResponseEntity<ApiResponse<List<Material>>> all() {
        List<Material> list = materialService.getAllMaterials();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Materials fetched successfully", list)
        );
    }

    // 📥 By Category
    @GetMapping("/getMaterialsCategory/{categoryId}")
    public ResponseEntity<ApiResponse<List<Material>>> byCategory(
            @PathVariable Long categoryId) {

        List<Material> list = materialService.getMaterialByCategory(categoryId);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Materials fetched by category", list)
        );
    }
}
