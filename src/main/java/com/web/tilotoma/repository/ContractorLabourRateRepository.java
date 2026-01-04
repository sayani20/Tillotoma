package com.web.tilotoma.repository;
import com.web.tilotoma.entity.labour.ContractorLabourRate;
import com.web.tilotoma.entity.labour.Contractor;
import com.web.tilotoma.entity.labour.LabourType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractorLabourRateRepository extends JpaRepository<ContractorLabourRate, Long> {
    List<ContractorLabourRate> findByContractor(Contractor contractor);
    Optional<ContractorLabourRate> findByContractorAndLabourType(Contractor contractor, LabourType labourType);



}