package com.web.tilotoma.serviceimpl;

import com.web.tilotoma.service.ConsumptionService;

import com.web.tilotoma.dto.ConsumptionRequestDto;
import com.web.tilotoma.dto.ConsumptionResponseDto;
import com.web.tilotoma.entity.material.Consumption;
import com.web.tilotoma.entity.material.Material;
import com.web.tilotoma.entity.material.StockLedger;
import com.web.tilotoma.entity.material.StockTxnType;
import com.web.tilotoma.repository.ConsumptionRepository;
import com.web.tilotoma.repository.MaterialRepository;
import com.web.tilotoma.repository.StockLedgerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsumptionServiceImpl implements ConsumptionService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ConsumptionRepository consumptionRepository;

    @Autowired
    private StockLedgerRepository stockLedgerRepository;

    @Override
    @Transactional
    public ConsumptionResponseDto consumeMaterial(
            ConsumptionRequestDto request) {

        // 1️⃣ Validate material
        Material material = materialRepository.findById(request.getMaterialId())
                .orElseThrow(() ->
                        new RuntimeException("Material not found"));

        // 2️⃣ Get current stock
        Double currentStock =
                stockLedgerRepository.getCurrentStock(material.getId());

        if (currentStock < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // 3️⃣ Save consumption entry
        Consumption consumption = Consumption.builder()
                .material(material)
                .quantity(request.getQuantity())
                .area(request.getArea())
                .tower(request.getTower())
                .consDate(
                        request.getConsDate() != null
                                ? request.getConsDate()
                                : LocalDateTime.now()
                )
                .build();

        consumptionRepository.save(consumption);

        // 4️⃣ Stock ledger OUT entry
        StockLedger ledger = StockLedger.builder()
                .material(material)
                .txnType(StockTxnType.OUT)
                .quantity(request.getQuantity())
                .reference("CONSUMPTION")
                .txnDate(LocalDateTime.now())
                .build();

        stockLedgerRepository.save(ledger);

        // 5️⃣ Remaining stock
        Double remainingStock =
                currentStock - request.getQuantity();

        return new ConsumptionResponseDto(
                material.getId(),
                request.getQuantity(),
                remainingStock
        );
    }

    @Override
    public List<ConsumptionRequestDto> getConsumptionByMaterial(Long materialId) {

        return consumptionRepository
                .findByMaterial_Id(materialId)
                .stream()
                .map(c -> ConsumptionRequestDto.builder()
                        .consumptionId(c.getId())
                        .materialId(c.getMaterial().getId())
                        .quantity(c.getQuantity())
                        .area(c.getArea())
                        .tower(c.getTower())
                        .consDate(c.getConsDate())
                        .build()
                )
                .toList();
    }
}

