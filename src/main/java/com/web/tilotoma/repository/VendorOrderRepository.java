package com.web.tilotoma.repository;

import com.web.tilotoma.dto.OrderHistoryResponseDto;
import com.web.tilotoma.dto.VendorBillReportResponseDto;
import com.web.tilotoma.entity.material.OrderStatus;
import com.web.tilotoma.entity.material.VendorOrder;
import com.web.tilotoma.service.OrderHistoryProjection;
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




    @Query("""
SELECT
    o.id                AS orderId,
    o.orderNumber       AS orderNumber,
    o.orderDate         AS orderDate,
    o.requiredBy        AS requiredBy,
    COUNT(DISTINCT i.id) AS noOfItems,
    o.totalAmount       AS totalAmount,

    (SELECT r1.receivedAmount
     FROM VendorOrderReceive r1
     WHERE r1.vendorOrder.id = o.id
       AND r1.receivedOn = (
           SELECT MAX(r2.receivedOn)
           FROM VendorOrderReceive r2
           WHERE r2.vendorOrder.id = o.id
       )
    ) AS paidAmount,

    (SELECT r3.remarks
     FROM VendorOrderReceive r3
     WHERE r3.vendorOrder.id = o.id
       AND r3.receivedOn = (
           SELECT MAX(r4.receivedOn)
           FROM VendorOrderReceive r4
           WHERE r4.vendorOrder.id = o.id
       )
    ) AS remarks,

    o.status            AS status,
    o.approvedOn        AS approvedOn,
    v.vendorName        AS vendorName,

    (SELECT r5.challanNumber
     FROM VendorOrderReceive r5
     WHERE r5.vendorOrder.id = o.id
       AND r5.receivedOn = (
           SELECT MAX(r6.receivedOn)
           FROM VendorOrderReceive r6
           WHERE r6.vendorOrder.id = o.id
       )
    ) AS challanNumber,

    (SELECT MAX(r7.receivedOn)
     FROM VendorOrderReceive r7
     WHERE r7.vendorOrder.id = o.id
    ) AS receivedOn,

    (SELECT r8.orderReceivedType
     FROM VendorOrderReceive r8
     WHERE r8.vendorOrder.id = o.id
       AND r8.receivedOn = (
           SELECT MAX(r9.receivedOn)
           FROM VendorOrderReceive r9
           WHERE r9.vendorOrder.id = o.id
       )
    ) AS orderReceivedType

FROM VendorOrder o
JOIN o.vendor v
LEFT JOIN o.items i
WHERE o.status = :status
  AND o.receivedOrder = true
  AND (:fromDate IS NULL OR o.orderDate >= :fromDate)
  AND (:toDate IS NULL OR o.orderDate <= :toDate)
GROUP BY
    o.id,
    o.orderNumber,
    o.orderDate,
    o.requiredBy,
    o.totalAmount,
    o.status,
    o.approvedOn,
    v.vendorName
ORDER BY o.orderDate DESC
""")
    List<OrderHistoryProjection> findOrderHistory(
            @Param("status") OrderStatus status,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );






    @Query("""
    SELECT
        v.id,
        v.vendorName,
        o.id,
        o.orderNumber,

        (SELECT MAX(r1.challanNumber)
         FROM VendorOrderReceive r1
         WHERE r1.vendorOrder.id = o.id),

        (SELECT COALESCE(SUM(r2.receivedAmount), 0)
         FROM VendorOrderReceive r2
         WHERE r2.vendorOrder.id = o.id),

        (SELECT COALESCE(SUM(p.paidAmount), 0)
         FROM VendorPayment p
         WHERE p.vendorOrder.id = o.id),

        (SELECT
            CASE
                WHEN COUNT(DISTINCT p2.paymentMode) > 1
                    THEN 'MULTIPLE'
                ELSE MAX(p2.paymentMode)
            END
         FROM VendorPayment p2
         WHERE p2.vendorOrder.id = o.id),

        o.orderDate,
        mc.name
    FROM VendorOrder o
    JOIN o.vendor v
    JOIN VendorOrderReceive r ON r.vendorOrder.id = o.id
    JOIN r.material m
    JOIN m.materialCategory mc
    WHERE o.receivedOrder = true
      AND o.status = com.web.tilotoma.entity.material.OrderStatus.APPROVED
      AND (:fromDate IS NULL OR o.orderDate >= :fromDate)
      AND (:toDate IS NULL OR o.orderDate <= :toDate)
    GROUP BY
        v.id, v.vendorName,
        o.id, o.orderNumber, o.orderDate,
        mc.name
    ORDER BY v.id, o.orderDate
""")
    List<Object[]> getVendorBillRaw(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );





}

