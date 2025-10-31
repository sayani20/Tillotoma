package com.web.tilotoma.controller;

import com.web.tilotoma.exceptions.CustomException;
import com.web.tilotoma.serviceimpl.SignUpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SignUpController {
    @Autowired
    private SignUpServiceImpl signUpService;
@GetMapping("/allRole")
public ResponseEntity<List<String>> getAllRoleNames() {
    try {
        List<String> names = signUpService.getAllRoleNames();
        return ResponseEntity.ok(names);
    }
    catch (Exception e){
        throw new CustomException("no data found");
    }
}
}
