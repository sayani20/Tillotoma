package com.web.tilotoma.controller;

import com.web.tilotoma.dto.ApiResponse;
import com.web.tilotoma.dto.ContractorRequest;
import com.web.tilotoma.dto.LabourRequest;
import com.web.tilotoma.dto.LabourTypeRequest;
import com.web.tilotoma.entity.Contractor;
import com.web.tilotoma.entity.Labour;
import com.web.tilotoma.entity.LabourAttendance;
import com.web.tilotoma.entity.LabourType;

import com.web.tilotoma.service.ContractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contractor")
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
    public ResponseEntity<ApiResponse<List<Contractor>>> getAllContractors() {
        List<Contractor> contractors = contractorService.getAllContractors();
        return ResponseEntity.ok(new ApiResponse<>(true, "All contractors fetched", contractors));
    }

    //contractorId wise labour add
    @PostMapping("/labour/addLabour")
    public ResponseEntity<ApiResponse<Labour>> addLabourUnderContractor(
            @RequestBody LabourRequest request) {

        Labour labour = contractorService.addLabourUnderContractor(request.getContractorId(), request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Labour added successfully", labour));
    }

    //getAllLabours
    @GetMapping("/labour/getAllLabours")
    public ResponseEntity<ApiResponse<List<Labour>>> getAllLabours() {
        List<Labour> labours = contractorService.getAllLabours();
        return ResponseEntity.ok(new ApiResponse<>(true, "All labours fetched", labours));
    }

    //getAllLabourTypes
    @GetMapping("/labourType/getAllLabourTypes")
    public ResponseEntity<ApiResponse<List<LabourType>>> getAllLabourTypes() {
        List<LabourType> types = contractorService.getAllLabourTypes();
        return ResponseEntity.ok(new ApiResponse<>(true, "All labour types fetched", types));
    }

    //addLabourType
    @PostMapping("/labourType/addLabourType")
    public ResponseEntity<ApiResponse<LabourType>> addLabourType(@RequestBody LabourTypeRequest request) {
        LabourType type = contractorService.addLabourType(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Labour type added successfully", type));
    }

    //contractorId wise labour
    @GetMapping("/contractorIdWiseLabour/{contractorId}")
    public ResponseEntity<List<Labour>> getLaboursByContractor(@PathVariable Long contractorId) {
        List<Labour> labours = contractorService.getLaboursByContractor(contractorId);
        return ResponseEntity.ok(labours);
    }

    //labour login logout api
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

}
