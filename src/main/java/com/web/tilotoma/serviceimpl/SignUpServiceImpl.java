package com.web.tilotoma.serviceimpl;

import com.web.tilotoma.dto.ApiResponse;
import com.web.tilotoma.dto.LoginDto;
import com.web.tilotoma.dto.RoleDto;
import com.web.tilotoma.dto.UserDto;
import com.web.tilotoma.entity.Role;
import com.web.tilotoma.entity.User;
import com.web.tilotoma.exceptions.ResourceNotFoundException;
import com.web.tilotoma.repository.RoleRepo;
import com.web.tilotoma.repository.UserRepo;
import com.web.tilotoma.service.SignUpService;
import com.web.tilotoma.utill.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SignUpServiceImpl implements SignUpService {

    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private UserRepo userRepo;

    @Override
    public List<RoleDto> getAllRoleNames() {
        List<Role> roles = roleRepo.findAll();
        if (roles.isEmpty()) {
            throw new RuntimeException("No roles found in the system.");
        }
        return roles.stream()
                .filter(Role::isActive)
                .map(role -> new RoleDto(role.getId(), role.getName()))
                .toList();
    }

    @Override
    public Role createRole(Role role) {
        if (roleRepo.existsByName(role.getName())) {
            throw new RuntimeException("Role already exists with name: " + role.getName());
        }
        if (role.getName() == null || role.getName().isBlank()) {
            throw new RuntimeException("Role name cannot be empty.");
        }
        return roleRepo.save(role);
    }

    @Override
    public List<User> getUsersByRoleId(Long roleId) {
        if (!roleRepo.existsById(roleId)) {
            throw new RuntimeException("Invalid Role ID: " + roleId);
        }
        List<User> users = userRepo.findByRole_Id(roleId);
        if (users.isEmpty()) {
            throw new RuntimeException("No users found for this role ID.");
        }
        return users;
    }

    @Override
    public User createUser(UserDto userDto) {
        // 🔹 Check if email already exists
        if (userRepo.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with email: " + userDto.getEmail());
        }

        // 🔹 Validate role
        Role role = roleRepo.findById(userDto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + userDto.getRoleId()));

        // 🔹 Validate password
        if (userDto.getPassword() == null || userDto.getPassword().isBlank()) {
            throw new RuntimeException("Password cannot be empty.");
        }

        // 🔹 Build user
        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(PasswordUtil.hashPassword(userDto.getPassword()))
                .mobileNumber(userDto.getMobileNumber())
                .aadharNumber(userDto.getAadharNumber())
                .role(role)
                .isActive(true)
                .lastUpdateRemarks(userDto.getLastUpdateRemarks())
                .build();

        // 🔹 Optional createdBy handling
        if (userDto.getCreatedBy() != null) {
            userRepo.findById(userDto.getCreatedBy())
                    .ifPresentOrElse(
                            user::setCreatedBy,
                            () -> System.out.println("⚠️ createdBy user not found, skipping...")
                    );
        }

        // 🔹 Save user
        return userRepo.save(user);
    }



    @Override
    public User login(LoginDto loginDto) {
        User user = userRepo.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("No account found with email: " + loginDto.getEmail()));

        if (user.getIsActive() == null || !user.getIsActive()) {
            throw new RuntimeException("This user account is deactivated. Please contact admin.");
        }

        boolean isPasswordMatch = PasswordUtil.checkPassword(loginDto.getPassword(), user.getPassword());
        if (!isPasswordMatch) {
            throw new RuntimeException("Incorrect password.");
        }

        if (!user.getRole().getId().equals(loginDto.getRoleid())) {
            throw new RuntimeException("Selected role does not match the user’s assigned role.");
        }

        return user;
    }

    @Override
    public User updateUserStatus(Long userId, Boolean isActive, String remarks) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setIsActive(isActive);
        user.setLastUpdateRemarks(remarks != null ? remarks : "");
        return userRepo.save(user);
    }

    public User getUserById(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    public User updateUser(Long userId, User updatedUser) {
        User existingUser = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Update only non-null fields
        if (updatedUser.getName() != null) existingUser.setName(updatedUser.getName());
        if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
        if (updatedUser.getMobileNumber() != null) existingUser.setMobileNumber(updatedUser.getMobileNumber());
        if (updatedUser.getAadharNumber() != null) existingUser.setAadharNumber(updatedUser.getAadharNumber());
        if (updatedUser.getRole() != null) existingUser.setRole(updatedUser.getRole());
        if (updatedUser.getPassword() != null) existingUser.setPassword(updatedUser.getPassword());
        if (updatedUser.getIsActive() != null) existingUser.setIsActive(updatedUser.getIsActive());
        if (updatedUser.getLastUpdateRemarks() != null)
            existingUser.setLastUpdateRemarks(updatedUser.getLastUpdateRemarks());

        return userRepo.save(existingUser);
    }

    public void deleteUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        userRepo.delete(user);
    }
}
