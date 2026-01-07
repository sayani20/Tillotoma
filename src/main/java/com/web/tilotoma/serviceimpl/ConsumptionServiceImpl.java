package com.web.tilotoma.serviceimpl;

import com.web.tilotoma.dto.ApiResponse;
import com.web.tilotoma.dto.ConsumptionRequestDto;
import com.web.tilotoma.dto.ConsumptionResponseDto;
import com.web.tilotoma.entity.material.*;
import com.web.tilotoma.repository.ConsumptionRepository;
import com.web.tilotoma.repository.MaterialRepository;
import com.web.tilotoma.repository.StockLedgerRepository;
import com.web.tilotoma.service.ConsumptionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConsumptionServiceImpl implements ConsumptionService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private StockLedgerRepository stockLedgerRepository;

    @Autowired
    private ConsumptionRepository consumptionRepository;

    /**
     * Consume material from stock
     * 1️⃣ Validate stock
     * 2️⃣ Save consumption
     * 3️⃣ Update stock ledger (OUT)
     * 4️⃣ Return updated stock
     */
    @Override
    @Transactional
    public ApiResponse<ConsumptionResponseDto> consumeMaterial(
            ConsumptionRequestDto request) {

        // 🔹 1. Validate material
        Material material = materialRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new RuntimeException("Material not found"));

        // 🔹 2. Check available stock
        Double availableStock =
                stockLedgerRepository.getAvailableStock(material.getId());

        if (availableStock == null) {
            availableStock = 0.0;
        }

        if (request.getQuantity() <= 0) {
            return new ApiResponse<>(
                    false,
                    "Consumption quantity must be greater than zero",
                    null
            );
        }

        if (request.getQuantity() > availableStock) {
            return new ApiResponse<>(
                    false,
                    "Insufficient stock. Available stock: " + availableStock,
                    null
            );
        }

        // 🔹 3. Save consumption entry
        Consumption consumption = Consumption.builder()
                .material(material)
                .quantity(request.getQuantity())
                .area(request.getArea())
                .tower(request.getTower())
                .consDate(
                        request.getConsDate() != null
                                ? request.getConsDate().atStartOfDay()
                                : LocalDateTime.now()
                )
                .build();

        consumptionRepository.save(consumption);

        // 🔹 4. Stock ledger OUT entry
        StockLedger ledger = StockLedger.builder()
                .material(material)
                .txnType(StockTxnType.OUT)
                .quantity(request.getQuantity())
                .rate(null) // consumption has no rate
                .reference("CONSUMPTION")
                .txnDate(LocalDateTime.now())
                .build();

        stockLedgerRepository.save(ledger);

        // 🔹 5. Fetch updated stock
        Double updatedStock =
                stockLedgerRepository.getAvailableStock(material.getId());

        if (updatedStock == null) {
            updatedStock = 0.0;
        }

        // 🔹 6. Prepare response DTO
        ConsumptionResponseDto responseDto =
                new ConsumptionResponseDto(
                        consumption.getId(),
                        material.getId(),
                        material.getMaterialName(),
                        material.getUnit(),
                        consumption.getQuantity(),
                        updatedStock,
                        consumption.getTower(),
                        consumption.getArea(),
                        consumption.getConsDate()
                );

        return new ApiResponse<>(
                true,
                "Material consumption recorded successfully",
                responseDto
        );
    }
}
