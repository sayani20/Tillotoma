package com.web.tilotoma.serviceimpl;

import com.web.tilotoma.dto.LoginDto;
import com.web.tilotoma.dto.RoleDto;
import com.web.tilotoma.dto.UserDto;
import com.web.tilotoma.entity.Role;
import com.web.tilotoma.entity.User;
import com.web.tilotoma.repository.RoleRepo;
import com.web.tilotoma.repository.UserRepo;
import com.web.tilotoma.service.SignUpService;
import com.web.tilotoma.utill.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
public class SignUpServiceImpl implements SignUpService {
@Autowired
    private RoleRepo roleRepo;
    @Autowired
    private UserRepo userRepo;
    @Override
    public List<RoleDto> getAllRoleNames() {
        return roleRepo.findAll()
                .stream()
                .filter(Role::isActive)
                .map(role -> new RoleDto(role.getId(), role.getName()))
                .toList();
    }

    public Role createRole(Role role) {
        if (roleRepo.existsByName(role.getName())) {
            throw new RuntimeException("Role already exists with name: " + role.getName());
        }
        return roleRepo.save(role);
    }

    @Override
    public List<User> getUsersByRoleId(Long roleId) {
        return userRepo.findByRole_Id(roleId);
    }

    @Override
    public User createUser(UserDto userDto) {
        Role role = roleRepo.findById(userDto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + userDto.getRoleId()));
        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(PasswordUtil.hashPassword(userDto.getPassword()))
                .mobileNumber(userDto.getMobileNumber())
                .aadharNumber(userDto.getAadharNumber())
                .role(role)
                .createdBy(userDto.getCreatedBy() != null ? userRepo.findById(userDto.getCreatedBy()).orElse(null) : null)
                .lastUpdateRemarks(userDto.getLastUpdateRemarks())
                .isActive(true)
                .build();
        return userRepo.save(user);

    }

    @Override
    public User login(LoginDto loginDto) {
        User user = userRepo.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));
        boolean isPasswordMatch = PasswordUtil.checkPassword(loginDto.getPassword(), user.getPassword());
        if (!isPasswordMatch) {
            throw new RuntimeException("Password not match");
        }
        if (!user.getRole().getId().equals(loginDto.getRoleid())) {
            throw new RuntimeException("Role not match for this user");
        }
        return user;
    }
}


