package com.web.tilotoma.controller;

import com.web.tilotoma.dto.ApiResponse;
import com.web.tilotoma.dto.LoginDto;
import com.web.tilotoma.dto.UserDto;
import com.web.tilotoma.entity.Role;
import com.web.tilotoma.entity.User;
import com.web.tilotoma.exceptions.CustomException;
import com.web.tilotoma.serviceimpl.SignUpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SignUpController {
    @Autowired
    private SignUpServiceImpl signUpService;
    @GetMapping("/allRole")
    public ResponseEntity<ApiResponse<List<String>>> getAllRoles() {
    List<String> roles = signUpService.getAllRoleNames();
    return ResponseEntity.ok(new ApiResponse<>(true, "All Roles fetched", roles));
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<ApiResponse<List<User>>> getUsersByRole(@PathVariable Long roleId) {
        List<User> users = signUpService.getUsersByRoleId(roleId);
        return ResponseEntity.ok(new ApiResponse<>(true, "All users fetched for role ID: " + roleId, users));
    }
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody UserDto userDto) {
        User savedUser = signUpService.createUser(userDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "User created successfully", savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginDto loginDto) {
        try {
            User loggedInUser = signUpService.login(loginDto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", loggedInUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


}
