package com.web.tilotoma.repository;
import com.web.tilotoma.dto.VendorOrderReceiveResponseDto;
import com.web.tilotoma.entity.material.StockLedger;
import com.web.tilotoma.entity.material.VendorOrderReceive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
public interface VendorOrderReceiveRepository extends JpaRepository<VendorOrderReceive, Long> {

    List<VendorOrderReceive> findByVendorOrder_Id(Long orderId);

    @Query("""
        SELECT COALESCE(SUM(r.receivedAmount), 0)
        FROM VendorOrderReceive r
        WHERE r.vendorOrder.id = :orderId
    """)
    Double getTotalReceivedAmount(@Param("orderId") Long orderId);

    List<StockLedger> findByMaterial_Id(Long materialId);


   /* @Query("""
        SELECT new com.web.tilotoma.dto.VendorOrderReceiveResponseDto(
            r.id,
            o.id,
            o.orderNumber,
            m.id,
            m.materialName,
            m.unit,
            r.receivedQuantity,
            r.receivedRate,
            r.receivedAmount,
            r.challanNumber,
            r.receivedOn
        )
        FROM VendorOrderReceive r
        JOIN r.vendorOrder o
        JOIN r.material m
        WHERE (:fromDate IS NULL OR r.receivedOn >= :fromDate)
          AND (:toDate IS NULL OR r.receivedOn <= :toDate)
        ORDER BY r.receivedOn DESC
    """)
    List<VendorOrderReceiveResponseDto> findReceivesBetweenDates(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );*/


    @Query("""
    SELECT new com.web.tilotoma.dto.VendorOrderReceiveResponseDto(
        MIN(r.id),
        o.id,
        r.challanNumber,
        v.vendorName,
        DATE(r.receivedOn),
        COUNT(r.id)
    )
    FROM VendorOrderReceive r
    JOIN r.vendorOrder o
    JOIN o.vendor v
    WHERE (:fromDate IS NULL OR r.receivedOn >= :fromDate)
      AND (:toDate IS NULL OR r.receivedOn <= :toDate)
    GROUP BY o.id, r.challanNumber, v.vendorName, DATE(r.receivedOn)
    ORDER BY MAX(r.receivedOn) DESC
""")
    List<VendorOrderReceiveResponseDto> findReceivesBetweenDates(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );
}