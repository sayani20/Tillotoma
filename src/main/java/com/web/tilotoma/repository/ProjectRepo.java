package com.web.tilotoma.repository;

import com.web.tilotoma.entity.labour.Contractor;
import com.web.tilotoma.entity.labour.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepo extends JpaRepository<Project,Long> {
    List<Project> findByContractorId(Long contractorId);
    //List<Project> findByContractor(Contractor contractor);

    // ✅ New query: fetch projects using labour-project relation
    @Query("""
           SELECT DISTINCT p FROM Project p
           JOIN p.labours l
           WHERE l.contractor = :contractor
           """)
    List<Project> findProjectsByLabourContractor(@Param("contractor") Contractor contractor);

    @Query("SELECT DISTINCT p FROM Labour l JOIN l.projects p WHERE l.contractor = :contractor")
    List<Project> findProjectsByContractorViaLabours(@Param("contractor") Contractor contractor);


    @Query("SELECT p FROM Project p WHERE p.contractor = :contractor")
    List<Project> findByContractor(@Param("contractor") Contractor contractor);

}
