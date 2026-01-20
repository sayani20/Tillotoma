package com.web.tilotoma.service;

import com.web.tilotoma.dto.ConsumptionRequestDto;
import com.web.tilotoma.dto.ConsumptionResponseDto;

import java.util.List;

public interface  ConsumptionService {
    ConsumptionResponseDto consumeMaterial(ConsumptionRequestDto request);
    List<ConsumptionRequestDto> getConsumptionByMaterial(Long materialId);
}
