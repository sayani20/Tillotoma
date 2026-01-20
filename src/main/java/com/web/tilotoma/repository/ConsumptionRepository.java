package com.web.tilotoma.repository;

import com.web.tilotoma.entity.material.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {
    List<Consumption> findByMaterial_Id(Long materialId);
}
