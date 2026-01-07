package com.web.tilotoma.repository;

import com.web.tilotoma.entity.material.VendorOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorOrderItemRepository
        extends JpaRepository<VendorOrderItem, Long> {
}
