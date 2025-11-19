package com.web.tilotoma.controller;

import com.web.tilotoma.serviceimpl.EsslService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EsslController {
    private final EsslService esslService;

    public EsslController(EsslService esslService) {
        this.esslService = esslService;
    }

    @GetMapping("/connect-device")
    public String connect() {
        esslService.connect();
        return "Connection method executed!";
    }
}
