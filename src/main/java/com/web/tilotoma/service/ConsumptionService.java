package com.web.tilotoma.service;

import com.web.tilotoma.dto.ConsumptionRequestDto;
import com.web.tilotoma.dto.ConsumptionResponseDto;

public interface  ConsumptionService {
    ConsumptionResponseDto consumeMaterial(ConsumptionRequestDto request);
}
