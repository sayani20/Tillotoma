package com.web.tilotoma.controller;

import com.web.tilotoma.dto.*;
import com.web.tilotoma.dto.bill.ContractorBillingResponse;
import com.web.tilotoma.dto.bill.LabourBillingDetailsResponse;
import com.web.tilotoma.dto.response.ContractorDetailsDto;
import com.web.tilotoma.entity.Contractor;
import com.web.tilotoma.entity.ContractorPayment;
import com.web.tilotoma.entity.LabourAttendance;
import com.web.tilotoma.service.ContractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/contractor")
//@CrossOrigin(origins = "*")
public class ContractorController {

    @Autowired
    private ContractorService contractorService;

    // ✅ Add Contractor
    @PostMapping("/addContractor")
    public ResponseEntity<ApiResponse<Contractor>> addContractor(@RequestBody ContractorRequest request) {
        try {
            Contractor contractor = contractorService.addContractor(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Contractor added successfully", contractor));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to add contractor", null));
        }
    }

    // ✅ Get All Contractors
    @GetMapping("/getAllContractors")
    public ResponseEntity<ApiResponse<List<ContractorResponse>>> getAllContractors() {
        try {
            List<ContractorResponse> contractors = contractorService.getAllContractorsCustom();
            return ResponseEntity.ok(new ApiResponse<>(true, "All contractors fetched successfully", contractors));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to fetch contractors", null));
        }
    }

    // ✅ Get Contractor Details by ID
    @GetMapping("/contractorDetails/{contractorId}")
    public ResponseEntity<ApiResponse<ContractorDetailsDto>> getContractorById(
            @PathVariable("contractorId") Long contractorId) {
        try {
            ContractorDetailsDto contractor = contractorService.getContractorById(contractorId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Contractor details fetched successfully", contractor));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to fetch contractor details", null));
        }
    }

    // ✅ Update Contractor by ID
    @PutMapping("/contractorDetails/update/{contractorId}")
    public ResponseEntity<ApiResponse<Contractor>> updateContractor(
            @PathVariable Long contractorId,
            @RequestBody ContractorRequest contractorRequest) {
        try {
            Contractor updatedContractor = contractorService.updateContractorDetails(contractorId, contractorRequest);
            return ResponseEntity.ok(new ApiResponse<>(true, "Contractor details updated successfully", updatedContractor));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to update contractor details", null));
        }
    }
    //1631

    // ✅ Delete Contractor by ID
    @DeleteMapping("/contractorDetails/delete/{contractorId}")
    public ResponseEntity<ApiResponse<String>> deleteContractor(@PathVariable("contractorId") Long contractorId) {
        try {
            contractorService.deleteContractorDetails(contractorId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Contractor deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to delete contractor", null));
        }
    }

    // ✅ Mark Attendance
    @PostMapping("/{labourId}/mark")
    public ResponseEntity<ApiResponse<LabourAttendance>> markAttendance(@PathVariable Long labourId) {
        try {
            LabourAttendance attendance = contractorService.markAttendance(labourId);
            String message = (attendance.getOutTime() == null)
                    ? "In-time marked successfully"
                    : "Out-time marked successfully";
            return ResponseEntity.ok(new ApiResponse<>(true, message, attendance));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to mark attendance", null));
        }
    }

    // ✅ Get Attendance by Labour ID
    @GetMapping("/{labourId}/report")
    public ResponseEntity<ApiResponse<List<LabourAttendance>>> getAttendanceReport(@PathVariable Long labourId) {
        try {
            List<LabourAttendance> records = contractorService.getAttendanceByLabour(labourId);
            String message = records.isEmpty()
                    ? "No attendance records found"
                    : "Attendance records fetched successfully";
            return ResponseEntity.ok(new ApiResponse<>(true, message, records));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to fetch attendance report", null));
        }
    }

    // ✅ Contractor Attendance Report Between Dates
    @PostMapping("/attendance-report")
    public ResponseEntity<ApiResponse<List<ContractorAttendanceReportDto>>> getContractorAttendanceReport(
            @RequestBody ContractorAttendanceReportRequest request) {
        try {
            List<ContractorAttendanceReportDto> report =
                    contractorService.getContractorAttendanceReportBetweenDates(
                            request.getContractorId(),
                            request.getStartDate(),
                            request.getEndDate()
                    );

            String message = report.isEmpty()
                    ? "No attendance records found in this date range"
                    : "Attendance report fetched successfully";

            return ResponseEntity.ok(new ApiResponse<>(true, message, report));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to fetch contractor attendance report", null));
        }
    }

    @GetMapping("/projectReport")
    public ResponseEntity<ApiResponse<List<ContractorProjectMonthlyReportDto>>> getContractorProjectReport(
            @RequestParam Long contractorId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        try {

            LocalDate now = LocalDate.now();
            int selectedYear = (year != null) ? year : now.getYear();
            int selectedMonth = (month != null) ? month : now.getMonthValue();

            List<ContractorProjectMonthlyReportDto> report =
                    contractorService.getMonthlyProjectReport(contractorId, selectedYear, selectedMonth);

            return ResponseEntity.ok(
                    ApiResponse.<List<ContractorProjectMonthlyReportDto>>builder()
                            .status(true)
                            .message("Monthly project report generated successfully.")
                            .data(report)
                            .build()
            );
        }
        catch (Exception e){
            throw new RuntimeException("Failed to generate bill"+e.getMessage());
        }
    }
    @PostMapping("/update-isCheck")
    public ResponseEntity<ApiResponse<String>> updateIsCheck(@RequestBody UpdateIsCheckRequest request) {
        try {
            String msg = contractorService.updateIsCheck(
                    request.getLabourId(),
                    request.getAttendanceDate(),
                    request.getIsCheck()
            );

            return ResponseEntity.ok(new ApiResponse<>(true, msg, null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }



    //-----------------

    @GetMapping("/billReportDateWise")
    public ApiResponse<List<ContractorBillingResponse>> getBillingReport(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        return contractorService.getBillingReport(fromDate, toDate);
    }

    @GetMapping("/labourBillingDetails")
    public ResponseEntity<ApiResponse<List<LabourBillingDetailsResponse>>> getLabourBillingDetails(
            @RequestParam Long contractorId,
            @RequestParam Long projectId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        List<LabourBillingDetailsResponse> report =
                contractorService.getLabourBillingDetails(contractorId, projectId, fromDate, toDate);

        return ResponseEntity.ok(new ApiResponse<>(true, "Labour billing details fetched successfully", report));
    }

    @PostMapping("/payment/update")
    public ApiResponse<String> updatePayment(@RequestBody PaymentRequest request) {
        return contractorService.updateContractorPayment(request);
    }

    @GetMapping("/payment/history")
    public ApiResponse<List<ContractorPaymentResponse>> getPaymentHistory(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        return contractorService.getPaymentHistory(fromDate, toDate);
    }
}
