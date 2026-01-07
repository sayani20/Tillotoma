package com.web.tilotoma.serviceimpl;

import com.web.tilotoma.dto.StockResponseDto;
import com.web.tilotoma.repository.StockLedgerRepository;
import com.web.tilotoma.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockLedgerRepository stockLedgerRepository;

    /**
     * Fetch current stock using ledger aggregation
     */
    @Override
    public List<StockResponseDto> getCurrentStock() {
        return stockLedgerRepository.getCurrentStock();
    }
}
