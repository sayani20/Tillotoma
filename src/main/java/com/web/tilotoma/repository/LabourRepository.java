package com.web.tilotoma.repository;

import com.web.tilotoma.entity.Labour;
import com.web.tilotoma.entity.Contractor;
import com.web.tilotoma.entity.LabourType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  LabourRepository extends JpaRepository<Labour, Long> {
    List<Labour> findByContractor(Contractor contractor);
    List<Labour> findByLabourType(LabourType labourType);
    List<Labour> findByIsActiveTrue();
    List<Labour> findByContractorId(Long contractorId);
    Optional<Labour> findByContractorIdAndLabourTypeId(Long contractorId, Long labourTypeId);


}
