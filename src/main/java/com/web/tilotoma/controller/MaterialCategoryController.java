package com.web.tilotoma.controller;
import com.web.tilotoma.dto.ApiResponse;
import com.web.tilotoma.dto.MaterialCategoryRequestDto;
import com.web.tilotoma.entity.material.MaterialCategory;
import com.web.tilotoma.service.MaterialCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material-category")
public class MaterialCategoryController {

    @Autowired
    private MaterialCategoryService categoryService;

    // ➕ Add Category
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<MaterialCategory>> addCategory(
            @RequestBody MaterialCategoryRequestDto requestDto) {
        try {
            MaterialCategory category = categoryService.addCategory(requestDto);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Material category added successfully", category)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to add material category", null));
        }
    }

    // 📥 Get All Categories
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<MaterialCategory>>> getAllCategories() {
        try {
            List<MaterialCategory> list = categoryService.getAllCategories();
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Material categories fetched successfully", list)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to fetch categories", null));
        }
    }
}
