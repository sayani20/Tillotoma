package com.web.tilotoma.controller;

import com.web.tilotoma.dto.ApiResponse;
import com.web.tilotoma.dto.ConsumptionRequestDto;
import com.web.tilotoma.dto.ConsumptionResponseDto;
import com.web.tilotoma.dto.StockResponseDto;

import com.web.tilotoma.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumption")
@RequiredArgsConstructor
public class ConsumptionController {


  /*  @Autowired
    private ConsumptionService consumptionService;*/

    @Autowired
    private StockService stockService;

  /*  @PostMapping("/save")
    public ResponseEntity<ApiResponse<ConsumptionResponseDto>> consumeMaterial(
            @RequestBody ConsumptionRequestDto request) {

        ApiResponse<ConsumptionResponseDto> response =
                consumptionService.consumeMaterial(request);

        return ResponseEntity.ok(response);
    }*/


    @GetMapping("/current")
    public ResponseEntity<ApiResponse<List<StockResponseDto>>> getCurrentStock() {
        List<StockResponseDto> stock = stockService.getCurrentStock();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Current stock fetched successfully", stock)
        );
    }
}
