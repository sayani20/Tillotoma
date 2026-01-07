package com.web.tilotoma.repository;

import com.web.tilotoma.entity.material.VendorOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VendorOrderRepository extends JpaRepository<VendorOrder, Long> {

    long countByOrderDate(LocalDate orderDate);

    // 🔹 Date range filter
    @Query("""
        SELECT o
        FROM VendorOrder o
        WHERE (:fromDate IS NULL OR o.orderDate >= :fromDate)
          AND (:toDate IS NULL OR o.orderDate <= :toDate)
        ORDER BY o.id DESC
    """)
    List<VendorOrder> findOrdersByDateRange(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );
}

