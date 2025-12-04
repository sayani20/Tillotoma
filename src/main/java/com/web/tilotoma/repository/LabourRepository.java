package com.web.tilotoma.repository;

import com.web.tilotoma.entity.Labour;
import com.web.tilotoma.entity.Contractor;
import com.web.tilotoma.entity.LabourType;
import com.web.tilotoma.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  LabourRepository extends JpaRepository<Labour, Long> {

    @Query("SELECT l FROM Labour l JOIN l.projects p WHERE p = :project AND l.contractor = :contractor")
    List<Labour> findByContractorAndProjects(@Param("contractor") Contractor contractor,
                                             @Param("project") Project project);



    List<Labour> findByContractor(Contractor contractor);
    List<Labour> findByLabourType(LabourType labourType);
    List<Labour> findByIsActiveTrue();
    List<Labour> findByContractorId(Long contractorId);
    Optional<Labour> findByContractorIdAndLabourTypeId(Long contractorId, Long labourTypeId);

    List<Labour> findByProjects(Project project);
    List<Labour> findByProjectsId(Long projectId); // used below



}
