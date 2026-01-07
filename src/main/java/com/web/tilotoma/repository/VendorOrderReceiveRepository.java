package com.web.tilotoma.repository;
import com.web.tilotoma.entity.material.StockLedger;
import com.web.tilotoma.entity.material.VendorOrderReceive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}