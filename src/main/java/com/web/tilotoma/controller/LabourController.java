package com.web.tilotoma.controller;

import com.web.tilotoma.dto.LabourRequest;
import com.web.tilotoma.entity.Labour;
import com.web.tilotoma.service.LabourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/labour")
public class LabourController {

    @Autowired
    private LabourService labourService;
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
    public ResponseEntity<?> updateLabour(@RequestParam Long id, @RequestBody LabourRequest labourRequest) {
        try {
            Labour updatedLabour = labourService.updateLabour(id, labourRequest);
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

}
