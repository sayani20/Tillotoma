package com.web.tilotoma.repository;
import com.web.tilotoma.entity.labour.LabourType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabourTypeRepository extends JpaRepository<LabourType, Long> {
    Optional<LabourType> findByTypeName(String typeName);
    List<LabourType> findByIsActiveTrue();
}
