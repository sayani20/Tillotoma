package com.web.tilotoma.repository;

import com.web.tilotoma.entity.material.VendorOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface VendorOrderRepository extends JpaRepository<VendorOrder, Long> {

    long countByOrderDate(LocalDate orderDate);
}

