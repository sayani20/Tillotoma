package com.web.tilotoma.controller;

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
@RequiredArgsConstructor
public class MaterialController {
    @Autowired
    private  MaterialService materialService;

    // ➕ Add material
    @PostMapping("/addMaterial")
    public String add(@RequestBody MaterialRequestDto req) {
        return materialService.addMaterial(req);
    }
    @PutMapping("/updateMaterial")
    public String update(@RequestBody MaterialRequestDto req) {
        return materialService.updateMaterial(req);
    }
    @DeleteMapping("/deleteMaterial/{id}")
    public String softDelete(@PathVariable Long id) {
        return materialService.deleteMaterial(id);
    }

    @GetMapping("/allMaterials")
    public List<Material> all() {
        return materialService.getAllMaterials();
    }

    @GetMapping("/getMaterials/{categoryId}")
    public List<Material> byCategory(@PathVariable Long categoryId) {
        return materialService.getMaterialByCategory(categoryId);
    }




}
