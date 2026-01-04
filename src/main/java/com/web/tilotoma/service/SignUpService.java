package com.web.tilotoma.service;

import com.web.tilotoma.dto.LoginDto;
import com.web.tilotoma.dto.RoleDto;
import com.web.tilotoma.dto.UserDto;
import com.web.tilotoma.entity.labour.Role;
import com.web.tilotoma.entity.labour.User;

import java.util.List;

public interface SignUpService {
    public List<RoleDto> getAllRoleNames();
    public List<User> getUsersByRoleId(Long roleId);
    public User createUser(UserDto userDto);
    User login(LoginDto loginDto);
    public User updateUserStatus(Long userId, Boolean isActive, String remarks);
    Role createRole(Role role);
    User getUserById(Long userId);
    User updateUser(Long userId, User updatedUser);
    void deleteUser(Long userId);
}
