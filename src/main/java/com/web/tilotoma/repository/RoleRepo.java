package com.web.tilotoma.repository;

import com.web.tilotoma.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepo extends JpaRepository<Role,Long> {
    @Query("SELECT u.name FROM Role u")
    List<String> findAllNames();
}
