package com.web.tilotoma.repository;

import com.web.tilotoma.dto.StockResponseDto;
import com.web.tilotoma.entity.material.StockLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StockLedgerRepository
        extends JpaRepository<StockLedger, Long> {

 /*   @Query("""
        SELECT COALESCE(SUM(
          CASE 
            WHEN s.txnType = 'IN' THEN s.quantity
            ELSE -s.quantity
          END
        ),0)
        FROM StockLedger s
        WHERE s.material.id = :materialId
    """)
    Double getCurrentStock(Long materialId);*/

    List<StockLedger> findByMaterial_Id(Long materialId);

    Optional<StockLedger> findTopByMaterial_IdOrderByTxnDateDesc(Long materialId);

    @Query("""
    SELECT COALESCE(SUM(
        CASE 
            WHEN s.txnType = 'IN' THEN s.quantity
            ELSE -s.quantity
        END
    ), 0)
    FROM StockLedger s
    WHERE s.material.id = :materialId
""")
    Double getAvailableStock(Long materialId);

    /*@Query("""
        SELECT new com.web.tilotoma.dto.StockResponseDto(
            m.id,
            m.materialName,
            m.unit,
            m.brand,
            MAX(s.txnDate),
            SUM(
                CASE 
                    WHEN s.txnType = 'IN' THEN s.quantity
                    ELSE -s.quantity
                END
            )
        )
        FROM StockLedger s
        JOIN s.material m
        GROUP BY m.id, m.materialName, m.unit, m.brand
        HAVING SUM(
            CASE 
                WHEN s.txnType = 'IN' THEN s.quantity
                ELSE -s.quantity
            END
        ) > 0
        ORDER BY MAX(s.txnDate) DESC
    """)
    List<StockResponseDto> getCurrentStock();*/

    @Query("""
    SELECT new com.web.tilotoma.dto.StockResponseDto(
        m.id,
        m.materialName,
        m.unit,
        m.brand,
        MAX(s.txnDate),
        SUM(
            CASE 
                WHEN s.txnType = 'IN' THEN s.quantity
                ELSE -s.quantity
            END
        ),
        m.minimumLimit
    )
    FROM StockLedger s
    JOIN s.material m
    GROUP BY 
        m.id,
        m.materialName,
        m.unit,
        m.brand,
        m.minimumLimit
    HAVING SUM(
        CASE 
            WHEN s.txnType = 'IN' THEN s.quantity
            ELSE -s.quantity
        END
    ) > 0
    ORDER BY MAX(s.txnDate) DESC
""")
    List<StockResponseDto> getCurrentStock();

    @Query("""
    SELECT COALESCE(SUM(
        CASE 
            WHEN s.txnType = 'IN' THEN s.quantity
            ELSE -s.quantity
        END
    ), 0)
    FROM StockLedger s
    WHERE s.material.id = :materialId
""")
    Double getCurrentStock(@Param("materialId") Long materialId);
}
