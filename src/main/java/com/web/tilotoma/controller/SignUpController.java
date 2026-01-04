package com.web.tilotoma.controller;

import com.web.tilotoma.dto.*;
import com.web.tilotoma.entity.labour.Role;
import com.web.tilotoma.entity.labour.User;
import com.web.tilotoma.repository.UserRepo;
import com.web.tilotoma.serviceimpl.SignUpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
//@CrossOrigin(origins = "*")
public class SignUpController {

    @Autowired
    private SignUpServiceImpl signUpService;
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/allRole")
    public ResponseEntity<?> getAllRoles() {
        try {
            List<RoleDto> roles = signUpService.getAllRoleNames();
            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "message", "All roles fetched successfully",
                    "data", roles
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", false,
                    "message", "Failed to fetch roles: " + e.getMessage(),
                    "data", null
            ));
        }
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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", false,
                    "message", "Failed to create role: " + e.getMessage(),
                    "data", null
            ));
        }
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<?> getUsersByRole(@PathVariable Long roleId) {
        try {
            List<User> users = signUpService.getUsersByRoleId(roleId);
            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "message", "Users fetched successfully for role ID: " + roleId,
                    "data", users
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", false,
                    "message", "Failed to fetch users: " + e.getMessage(),
                    "data", null
            ));
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        try {
            User savedUser = signUpService.createUser(userDto);
            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "message", "User created successfully",
                    "data", savedUser
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", false,
                    "message", "Failed to create user: " + e.getMessage(),
                    "data", null
            ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            User loggedInUser = signUpService.login(loginDto);
            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "message", "Login successful",
                    "data", loggedInUser
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", false,
                    "message", "Login failed: " + e.getMessage(),
                    "data", null
            ));
        }
    }

    @PostMapping("/updateUserStatus")
    public ResponseEntity<?> updateUserStatus(@RequestBody UserStatusUpdateRequest request) {
        try {
            User updatedUser = signUpService.updateUserStatus(request.getUserId(), request.getIsActive(), request.getRemarks());
            String message = request.getIsActive()
                    ? "User activated successfully"
                    : "User deactivated successfully";
            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "message", message,
                    "data", updatedUser
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", false,
                    "message", "Failed to update user status: " + e.getMessage(),
                    "data", null
            ));
        }
    }

    @GetMapping("/getAllTypeUsers")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userRepo.findAll();
            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "message", "All users fetched successfully",
                    "data", users
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", false,
                    "message", "Failed to fetch users: " + e.getMessage(),
                    "data", null
            ));
        }
    }
    @GetMapping("userDetails/{userId}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long userId) {
        try {
            User user = signUpService.getUserById(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "User details fetched successfully", user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to fetch user details", null));
        }
    }

    // 🟡 Update User
    @PutMapping("updateUserDetails/{userId}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable Long userId,
            @RequestBody User updatedUser
    ) {
        try {
            User user = signUpService.updateUser(userId, updatedUser);
            return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>(false, "Failed to update user", null));
        }
    }

    // 🔴 Delete User
    @DeleteMapping("deleteUser/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        try {
            signUpService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>(false, "Failed to delete user", null));
        }
    }

}
