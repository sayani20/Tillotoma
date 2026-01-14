package com.web.tilotoma.repository;

import com.web.tilotoma.dto.OrderHistoryResponseDto;
import com.web.tilotoma.dto.VendorBillReportResponseDto;
import com.web.tilotoma.entity.material.OrderStatus;
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


    @Query("""
    SELECT new com.web.tilotoma.dto.OrderHistoryResponseDto(
        o.id,
        o.orderNumber,
        o.orderDate,
        o.requiredBy,
        COUNT(i.id),
        o.totalAmount,
        o.remarks,
        o.status,
        o.approvedOn
    )
    FROM VendorOrder o
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
        o.remarks,
        o.status,
        o.approvedOn
    ORDER BY o.orderDate DESC
""")
    List<OrderHistoryResponseDto> findOrderHistory(
            @Param("status") OrderStatus status,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );



    /*@Query("""
        SELECT new com.web.tilotoma.dto.VendorBillReportResponseDto(
            v.id,
            v.vendorName,
            o.id,
            o.orderNumber,
            MAX(r.challanNumber),
            SUM(r.receivedAmount),
            COALESCE(SUM(p.paidAmount), 0),
            (SUM(r.receivedAmount) - COALESCE(SUM(p.paidAmount), 0)),
            (SUM(r.receivedAmount) - COALESCE(SUM(p.paidAmount), 0)),
            o.orderDate
        )
        FROM VendorOrder o
        JOIN o.vendor v
        JOIN VendorOrderReceive r ON r.vendorOrder.id = o.id
        LEFT JOIN VendorPayment p ON p.vendorOrder.id = o.id
        WHERE o.receivedOrder = true
          AND (:fromDate IS NULL OR o.orderDate >= :fromDate)
          AND (:toDate IS NULL OR o.orderDate <= :toDate)
        GROUP BY
            v.id,
            v.vendorName,
            o.id,
            o.orderNumber,
            o.orderDate
        ORDER BY o.orderDate DESC
    """)
    List<VendorBillReportResponseDto> getVendorBillReport(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );*/

    @Query("""
        SELECT
            v.id,
            v.vendorName,
            o.id,
            o.orderNumber,

            /* challan number */
            (
                SELECT MAX(r.challanNumber)
                FROM VendorOrderReceive r
                WHERE r.vendorOrder.id = o.id
            ),

            /* total received amount */
            (
                SELECT COALESCE(SUM(r.receivedAmount), 0)
                FROM VendorOrderReceive r
                WHERE r.vendorOrder.id = o.id
            ),

            /* total paid amount */
            (
                SELECT COALESCE(SUM(p.paidAmount), 0)
                FROM VendorPayment p
                WHERE p.vendorOrder.id = o.id
            ),

            /* payment mode summary */
            (
                SELECT
                    CASE
                        WHEN COUNT(DISTINCT p.paymentMode) > 1
                            THEN 'MULTIPLE'
                        ELSE MAX(p.paymentMode)
                    END
                FROM VendorPayment p
                WHERE p.vendorOrder.id = o.id
            ),

            o.orderDate
        FROM VendorOrder o
        JOIN o.vendor v
        WHERE o.receivedOrder = true
          AND o.status = com.web.tilotoma.entity.material.OrderStatus.APPROVED
          AND (:fromDate IS NULL OR o.orderDate >= :fromDate)
          AND (:toDate IS NULL OR o.orderDate <= :toDate)
        ORDER BY v.id, o.orderDate
    """)
    List<Object[]> getVendorBillRaw(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );





}

