package com.web.tilotoma.repository;
import com.web.tilotoma.entity.material.MaterialCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  MaterialCategoryRepository extends JpaRepository<MaterialCategory, Long> {

    Optional<MaterialCategory> findByName(String name);
}
