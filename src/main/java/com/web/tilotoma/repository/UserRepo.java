package com.web.tilotoma.repository;

import com.web.tilotoma.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    List<User> findByRole_Id(Long roleId);
    Optional<User> findByEmail(String email);
}
