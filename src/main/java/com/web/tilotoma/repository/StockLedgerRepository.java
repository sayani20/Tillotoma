package com.web.tilotoma.repository;

import com.web.tilotoma.entity.material.StockLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockLedgerRepository
        extends JpaRepository<StockLedger, Long> {

    @Query("""
        SELECT COALESCE(SUM(
          CASE 
            WHEN s.txnType = 'IN' THEN s.quantity
            ELSE -s.quantity
          END
        ),0)
        FROM StockLedger s
        WHERE s.material.id = :materialId
    """)
    Double getCurrentStock(Long materialId);
    List<StockLedger> findByMaterial_Id(Long materialId);

    Optional<StockLedger> findTopByMaterial_IdOrderByTxnDateDesc(Long materialId);
}
