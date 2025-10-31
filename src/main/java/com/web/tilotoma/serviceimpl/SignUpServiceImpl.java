package com.web.tilotoma.serviceimpl;

import com.web.tilotoma.repository.RoleRepo;
import com.web.tilotoma.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignUpServiceImpl implements SignUpService {
@Autowired
    private RoleRepo roleRepo;
    @Override
    public List<String> getAllRoleNames() {
        return roleRepo.findAllNames();
    }
}
