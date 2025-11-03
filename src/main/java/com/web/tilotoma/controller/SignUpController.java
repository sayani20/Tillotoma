package com.web.tilotoma.controller;

import com.web.tilotoma.dto.*;
import com.web.tilotoma.entity.Role;
import com.web.tilotoma.entity.User;
import com.web.tilotoma.exceptions.CustomException;
import com.web.tilotoma.repository.UserRepo;
import com.web.tilotoma.serviceimpl.SignUpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class SignUpController {
    @Autowired
    private SignUpServiceImpl signUpService;
    @Autowired
    private UserRepo userRepo;


    @GetMapping("/allRole")
    public ResponseEntity<ApiResponse<List<RoleDto>>> getAllRoles() {
        List<RoleDto> roles = signUpService.getAllRoleNames();
        return ResponseEntity.ok(new ApiResponse<>(true, "All Roles fetched", roles));
    }

    @PostMapping("/createRole")
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        try {
            Role savedRole = signUpService.createRole(role);
            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "message", "Role created successfully",
                    "data", savedRole
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", false,
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<ApiResponse<List<User>>> getUsersByRole(@PathVariable Long roleId) {
        List<User> users = signUpService.getUsersByRoleId(roleId);
        return ResponseEntity.ok(new ApiResponse<>(true, "All users fetched for role ID: " + roleId, users));
    }

    @PostMapping("/createUser")
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

    @PostMapping("/updateUserStatus")
    public ResponseEntity<ApiResponse<User>> updateUserStatus(@RequestBody UserStatusUpdateRequest request) {
        User updatedUser = signUpService.updateUserStatus(request.getUserId(), request.getIsActive(), request.getRemarks());

        String message = request.getIsActive()
                ? "User activated successfully"
                : "User deactivated successfully";

        return ResponseEntity.ok(new ApiResponse<>(true, message, updatedUser));
    }

    @GetMapping("/getAllTypeUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepo.findAll();
        return ResponseEntity.ok(users);
    }
}



