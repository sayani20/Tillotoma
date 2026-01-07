package com.web.tilotoma.repository;

import com.web.tilotoma.entity.material.VendorPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VendorPaymentRepository
        extends JpaRepository<VendorPayment, Long> {

    @Query("""
        SELECT COALESCE(SUM(vp.paidAmount),0)
        FROM VendorPayment vp
        WHERE vp.vendorOrder.id = :orderId
    """)
    Double getTotalPaidByOrder(Long orderId);
}
