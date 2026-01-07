package com.web.tilotoma.controller;

import com.web.tilotoma.dto.ConsumptionRequestDto;
import com.web.tilotoma.service.ConsumptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consumption")
@RequiredArgsConstructor
public class ConsumptionController {

    private final ConsumptionService consumptionService;

    @PostMapping("/consume")
    public ResponseEntity<String> consume(
            @RequestBody ConsumptionRequestDto dto) {

        return ResponseEntity.ok(
                consumptionService.consumeMaterial(dto)
        );
    }
}
