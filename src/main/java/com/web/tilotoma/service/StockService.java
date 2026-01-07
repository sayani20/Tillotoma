package com.web.tilotoma.service;

import com.web.tilotoma.dto.StockResponseDto;

import java.util.List;

public interface StockService {

    /**
     * Get current available stock (material wise)
     * quantity = SUM(IN) - SUM(OUT)
     */
    List<StockResponseDto> getCurrentStock();
}
