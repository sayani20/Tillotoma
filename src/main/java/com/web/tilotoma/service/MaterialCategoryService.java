package com.web.tilotoma.service;

import com.web.tilotoma.dto.MaterialCategoryRequestDto;
import com.web.tilotoma.entity.material.MaterialCategory;

import java.util.List;

public interface MaterialCategoryService {

    MaterialCategory addCategory(MaterialCategoryRequestDto requestDto);

    List<MaterialCategory> getAllCategories();
}
