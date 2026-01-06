package com.web.tilotoma.repository;

import com.web.tilotoma.entity.material.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  MaterialRepository extends JpaRepository<Material, Long> {

    boolean existsByMaterialNameIgnoreCase(String materialName);

    List<Material> findByIsActiveTrue();
    List<Material> findByMaterialCategory_IdAndIsActiveTrue(Long categoryId);
}