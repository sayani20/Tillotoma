package com.web.tilotoma.repository;

import com.web.tilotoma.entity.material.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {
}
