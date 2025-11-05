package com.web.tilotoma.controller;

import com.web.tilotoma.dto.*;
import com.web.tilotoma.entity.Contractor;
import com.web.tilotoma.entity.Labour;
import com.web.tilotoma.entity.LabourAttendance;
import com.web.tilotoma.entity.LabourType;
import com.web.tilotoma.dto.*;
import com.web.tilotoma.entity.*;

import com.web.tilotoma.service.ContractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contractor")
@CrossOrigin(origins = "*")
public class ContractorController {

    @Autowired
    private ContractorService contractorService;

    //addContractor
    @PostMapping("/addContractor")
    public ResponseEntity<ApiResponse<Contractor>> addContractor(@RequestBody ContractorRequest request) {
        Contractor contractor = contractorService.addContractor(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Contractor added successfully", contractor));
    }

    //getAllContractors
    @GetMapping("/getAllContractors")
    public ResponseEntity<ApiResponse<List<ContractorResponse>>> getAllContractors() {
        List<ContractorResponse> contractors = contractorService.getAllContractorsCustom();
        return ResponseEntity.ok(new ApiResponse<>(true, "All contractors fetched", contractors));
    }

    //contractorId wise details
    @GetMapping("/contractorDetails/{contractorId}")
    public ResponseEntity<ApiResponse<Contractor>> getContractorById(@PathVariable("contractorId") Long contractorId) {
        Contractor contractor = contractorService.getContractorById(contractorId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Contractor details fetched successfully", contractor));
    }

    //contractorId wise details update
    @PutMapping("/contractorDetails/update/{contractorId}")
    public ResponseEntity<ApiResponse<Contractor>> updateContractor(
            @PathVariable Long contractorId,
            @RequestBody ContractorRequest contractorRequest) {

        Contractor updatedContractor = contractorService.updateContractorDetails(contractorId, contractorRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Contractor Details updated successfully", updatedContractor));
    }

    //contractorId wise delete contractor
    @DeleteMapping("/contractorDetails/delete/{contractorId}")
    public ResponseEntity<ApiResponse<String>> deleteContractor(@PathVariable("contractorId") Long contractorId) {
        contractorService.deleteContractorDetails(contractorId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Contractor Details deleted successfully", null));
    }


    //mark attendance
    @PostMapping("/{labourId}/mark")
    public ResponseEntity<?> markAttendance(@PathVariable Long labourId) {
        try {
            LabourAttendance attendance = contractorService.markAttendance(labourId);
            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "message", attendance.getOutTime() == null
                            ? "In-time marked successfully"
                            : "Out-time marked successfully",
                    "data", attendance
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", false,
                    "message", e.getMessage()
            ));
        }
    }

    //labourId wise attendance
    @GetMapping("/{labourId}/report")
    public ResponseEntity<?> getAttendanceReport(@PathVariable Long labourId) {
        try {
            List<LabourAttendance> records = contractorService.getAttendanceByLabour(labourId);
            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "message", records.isEmpty() ? "No attendance records found" : "Attendance records fetched successfully",
                    "data", records
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", false,
                    "message", e.getMessage()
            ));
        }
    }

    //proper attendance report
    @PostMapping("/attendance-report")
    public ResponseEntity<?> getContractorAttendanceReport(@RequestBody ContractorAttendanceReportRequest request) {
        try {
            List<ContractorAttendanceReportDto> report =
                    contractorService.getContractorAttendanceReportBetweenDates(
                            request.getContractorId(),
                            request.getStartDate(),
                            request.getEndDate()
                    );

            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "message", report.isEmpty()
                            ? "No attendance records found in this date range"
                            : "Attendance report fetched successfully",
                    "data", report
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", false,
                    "message", e.getMessage()
            ));
        }
    }
}
