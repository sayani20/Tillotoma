package com.web.tilotoma.repository;

import com.web.tilotoma.entity.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ContractorRepository extends JpaRepository<Contractor, Long> {
    Optional<Contractor> findByEmail(String email);
    Optional<Contractor> findByUsername(String username);
    List<Contractor> findByIsActiveTrue();
}