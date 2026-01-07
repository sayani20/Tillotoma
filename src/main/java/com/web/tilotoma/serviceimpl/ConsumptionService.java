package com.web.tilotoma.service;

import com.web.tilotoma.dto.ConsumptionRequestDto;
import com.web.tilotoma.entity.material.*;
import com.web.tilotoma.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConsumptionService {

    private final StockLedgerRepository stockLedgerRepository;
    private final ConsumptionRepository consumptionRepository;
    private final MaterialRepository materialRepository;

    @Transactional
    public String consumeMaterial(ConsumptionRequestDto dto) {

        // 1️⃣ Material check
        Material material = materialRepository.findById(dto.getMaterialId())
                .orElseThrow(() -> new RuntimeException("Material not found"));

        // 2️⃣ Latest stock from ledger
        StockLedger latestLedger =
                stockLedgerRepository
                        .findTopByMaterial_IdOrderByTxnDateDesc(dto.getMaterialId())
                        .orElseThrow(() -> new RuntimeException("No StockLedger entry found"));

        Double currentStock = latestLedger.getQuantity();

        // 3️⃣ Validation
        if (dto.getQuantity() > currentStock) {
            throw new RuntimeException("Insufficient stock");
        }

        // 4️⃣ Stock Minus → নতুন ledger row create (optional but good practice)
        Double remaining = currentStock - dto.getQuantity();

        StockLedger newLedger = StockLedger.builder()
                .material(material)
                .txnType(StockTxnType.OUT)
                .quantity(remaining)
                .rate(dto.getQuantity())
                .reference("CONSUMPTION")
                .txnDate(LocalDateTime.now())
                .build();

        stockLedgerRepository.save(newLedger);

        // 5️⃣ Insert into Consumption table
        Consumption cons = Consumption.builder()
                .quantity(dto.getQuantity())
                .area(dto.getArea())
                .tour(dto.getTour())
                        .consDate(dto.getConsDate() != null
                                ? dto.getConsDate()
                                : LocalDateTime.now())
                        .build();

        consumptionRepository.save(cons);

        return "Consumption saved. Remaining stock: " + remaining;
    }
}
