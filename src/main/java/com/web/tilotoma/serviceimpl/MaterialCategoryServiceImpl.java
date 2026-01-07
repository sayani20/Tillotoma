package com.web.tilotoma.serviceimpl;

import com.web.tilotoma.dto.MaterialCategoryRequestDto;
import com.web.tilotoma.entity.material.MaterialCategory;
import com.web.tilotoma.repository.MaterialCategoryRepository;
import com.web.tilotoma.service.MaterialCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class MaterialCategoryServiceImpl implements MaterialCategoryService {

    @Autowired
    private MaterialCategoryRepository categoryRepository;

    @Override
    public MaterialCategory addCategory(MaterialCategoryRequestDto requestDto) {

        MaterialCategory category = new MaterialCategory();
        category.setName(requestDto.getName());
        category.setIsActive(true);
        category.setCreatedOn(LocalDateTime.now());

        return categoryRepository.save(category);
    }

    @Override
    public List<MaterialCategory> getAllCategories() {
        return categoryRepository.findAll();
    }
}
