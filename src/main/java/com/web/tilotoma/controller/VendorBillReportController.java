package com.web.tilotoma.controller;


import com.web.tilotoma.dto.ApiResponse;
import com.web.tilotoma.dto.VendorBillReportResponseDto;
import com.web.tilotoma.service.VendorBillReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bill")
public class VendorBillReportController {

    private final VendorBillReportService service;

    public VendorBillReportController(
            VendorBillReportService service) {
        this.service = service;
    }

    /*@GetMapping("/vendor-report")
    public ApiResponse<List<VendorBillReportResponseDto>> getReport(

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate
    ) {

        return new ApiResponse<>(
                true,
                "Vendor bill report fetched successfully",
                service.getVendorBillReport(fromDate, toDate)
        );
    }*/

    @GetMapping("/vendor-report")
    public ApiResponse<List<VendorBillReportResponseDto>> getVendorBillReport(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate
    ) {
        return new ApiResponse<>(
                true,
                "Vendor bill report fetched successfully",
                service.getVendorBillReport(fromDate, toDate)
        );
    }
}

