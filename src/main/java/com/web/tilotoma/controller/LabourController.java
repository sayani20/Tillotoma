package com.web.tilotoma.controller;

import com.web.tilotoma.dto.ApiResponse;
import com.web.tilotoma.dto.LabourRequest;
import com.web.tilotoma.dto.LabourResponse;
import com.web.tilotoma.dto.LabourTypeRequest;
import com.web.tilotoma.entity.Labour;
import com.web.tilotoma.entity.LabourType;
import com.web.tilotoma.service.LabourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labour")
public class LabourController {

    @Autowired
    private LabourService labourService;


    //contractorId wise labour add
    @PostMapping("/labour/addLabour")
    public ResponseEntity<ApiResponse<Labour>> addLabourUnderContractor(
            @RequestBody LabourRequest request) {

        Labour labour = labourService.addLabourUnderContractor(request.getContractorId(), request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Labour added successfully", labour));
    }

    @GetMapping("/IdWiseLabourDetails")
    public ResponseEntity<?> getLabourDetails(@RequestParam Long id) {
        try {
            Labour labour = labourService.getLabourById(id);
            return ResponseEntity.ok(labour);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/updateLabourDetails")
    public ResponseEntity<?> updateLabour( @RequestBody LabourRequest labourRequest) {
        try {
            Labour updatedLabour = labourService.updateLabour(labourRequest.getId(), labourRequest);
            return ResponseEntity.ok(updatedLabour);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteLabour")
    public ResponseEntity<?> deleteLabour(@RequestParam Long id) {
        try {
            String message = labourService.deleteLabour(id);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //getAllLabours
    @GetMapping("/labour/getAllLabours")
    public ResponseEntity<ApiResponse<List<LabourResponse>>> getAllLabours() {
        List<LabourResponse> labours = labourService.getAllLabours();
        return ResponseEntity.ok(new ApiResponse<>(true, "All labours fetched", labours));
    }

    //contractorId wise labour
    @GetMapping("/contractorIdWiseLabour/{contractorId}")
    public ResponseEntity<ApiResponse<List<Labour>>> getLaboursByContractor(@PathVariable Long contractorId) {
        List<Labour> labours = labourService.getLaboursByContractor(contractorId);
        return ResponseEntity.ok(new ApiResponse<>(true, "All labours fetched for contractor ID: " + contractorId, labours));
    }

    //getAllLabourTypes
    @GetMapping("/labourType/getAllLabourTypes")
    public ResponseEntity<ApiResponse<List<LabourType>>> getAllLabourTypes() {
        List<LabourType> types = labourService.getAllLabourTypes();
        return ResponseEntity.ok(new ApiResponse<>(true, "All labour types fetched", types));
    }

    //addLabourType
    @PostMapping("/labourType/addLabourType")
    public ResponseEntity<ApiResponse<LabourType>> addLabourType(@RequestBody LabourTypeRequest request) {
        LabourType type = labourService.addLabourType(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Labour type added successfully", type));
    }

}
