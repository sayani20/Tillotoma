package com.web.tilotoma.controller;

import com.web.tilotoma.dto.*;
import com.web.tilotoma.dto.response.LabourResponseDto;
import com.web.tilotoma.entity.labour.Labour;
import com.web.tilotoma.entity.labour.LabourType;
import com.web.tilotoma.service.LabourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/labour")
//@CrossOrigin(origins = "*")
public class LabourController {

    @Autowired
    private LabourService labourService;

    // ✅ Add Labour under Contractor
    @PostMapping("/labour/addLabour")
    public ResponseEntity<ApiResponse<Labour>> addLabourUnderContractor(@RequestBody LabourRequest request) {
        try {
            Labour labour = labourService.addLabourUnderContractor(request.getContractorId(), request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Labour added successfully", labour));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to add labour", null));
        }
    }

    // ✅ Get Labour Details by ID
    @GetMapping("/IdWiseLabourDetails")
    public ResponseEntity<ApiResponse<Labour>> getLabourDetails(@RequestParam Long id) {
        try {
            Labour labour = labourService.getLabourById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Labour details fetched successfully", labour));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to fetch labour details", null));
        }
    }

    // ✅ Update Labour Details
    @PutMapping("/updateLabourDetails")
    public ResponseEntity<ApiResponse<Labour>> updateLabour(@RequestBody LabourRequest labourRequest) {
        try {
            Labour updatedLabour = labourService.updateLabour(labourRequest.getId(), labourRequest);
            return ResponseEntity.ok(new ApiResponse<>(true, "Labour details updated successfully", updatedLabour));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to update labour details", null));
        }
    }

    // ✅ Delete Labour by ID
    @DeleteMapping("/deleteLabour")
    public ResponseEntity<ApiResponse<String>> deleteLabour(@RequestParam Long id) {
        try {
            String message = labourService.deleteLabour(id);
            return ResponseEntity.ok(new ApiResponse<>(true, message, null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to delete labour", null));
        }
    }

    // ✅ Get All Labours
    @GetMapping("/labour/getAllLabours")
    public ResponseEntity<ApiResponse<List<LabourResponse>>> getAllLabours() {
        try {
            List<LabourResponse> labours = labourService.getAllLabours();
            return ResponseEntity.ok(new ApiResponse<>(true, "All labours fetched successfully", labours));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to fetch labours", null));
        }
    }

    // ✅ Get Labours by Contractor ID
    @GetMapping("/contractorIdWiseLabour/{contractorId}")
    public ResponseEntity<ApiResponse<List<LabourResponseDto>>> getLaboursByContractor(@PathVariable Long contractorId) {
        try {
            List<LabourResponseDto> labours = labourService.getLaboursByContractor(contractorId);
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "All labours fetched for contractor ID: " + contractorId,
                    labours
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to fetch labours for contractor", null));
        }
    }

    // ✅ Get All Labour Types
    @GetMapping("/labourType/getAllLabourTypes")
    public ResponseEntity<ApiResponse<List<LabourType>>> getAllLabourTypes() {
        try {
            List<LabourType> types = labourService.getAllLabourTypes();
            return ResponseEntity.ok(new ApiResponse<>(true, "All labour types fetched successfully", types));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to fetch labour types", null));
        }
    }

    // ✅ Add Labour Type
    @PostMapping("/labourType/addLabourType")
    public ResponseEntity<ApiResponse<LabourType>> addLabourType(@RequestBody LabourTypeRequest request) {
        try {
            LabourType type = labourService.addLabourType(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Labour type added successfully", type));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to add labour type", null));
        }
    }

    @GetMapping("/labourReport")
    public ResponseEntity<ApiResponse<List<LabourMonthlyReportDto>>> getMonthlyReport(
            @RequestParam Long contractorId,
            @RequestParam int year,
            @RequestParam int month) {

        try {
            List<LabourMonthlyReportDto> reports = labourService.getMonthlyReport(contractorId, year, month);

            return ResponseEntity.ok(
                    ApiResponse.<List<LabourMonthlyReportDto>>builder()
                            .status(true)
                            .message("Monthly report generated successfully.")
                            .data(reports)
                            .build()
            );

        }
        catch (Exception e) {
            throw new RuntimeException("Failed to generate report: " + e.getMessage());
        }
    }


    // ✅ Update Labour Type
    @PutMapping("/labourType/updateLabourType/{id}")
    public ResponseEntity<ApiResponse<LabourType>> updateLabourType(
            @PathVariable Long id,
            @RequestBody LabourTypeRequest request) {
        try {
            LabourType updatedType = labourService.updateLabourType(id, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Labour type updated successfully", updatedType));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to update labour type", null));
        }
    }

    // ✅ Activate / Deactivate Labour Type
    @PutMapping("/labourType/activeStatus/{id}")
    public ResponseEntity<ApiResponse<LabourType>> toggleLabourTypeStatus(
            @PathVariable Long id,
            @RequestParam boolean isActive) {
        try {
            LabourType updatedType = labourService.toggleLabourTypeStatus(id, isActive);
            String message = isActive ? "Labour type activated" : "Labour type deactivated";
            return ResponseEntity.ok(new ApiResponse<>(true, message, updatedType));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to update status", null));
        }
    }

    // ✅ Delete Labour Type
    @DeleteMapping("/labourType/deleteLabourType/{id}")
    public ResponseEntity<ApiResponse<String>> deleteLabourType(@PathVariable Long id) {
        try {
            String msg = labourService.deleteLabourType(id);
            return ResponseEntity.ok(new ApiResponse<>(true, msg, null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to delete labour type", null));
        }
    }
    @PostMapping("/updateCustomAmount")
    public ResponseEntity<?> updateCustomAmount(@RequestBody UpdateCustomAmountRequest req) {

        String msg = labourService.updateCustomAmount(
                req.getLabourId(),
                req.getAttendanceDate(),
                req.getCustomAmount(),
                req.getPaymentMethod(),
                req.getRemarks()
        );

        return ResponseEntity.ok(Map.of(
                "status", true,
                "message", msg
        ));
    }




}
