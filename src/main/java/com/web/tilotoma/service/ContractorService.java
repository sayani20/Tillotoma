package com.web.tilotoma.service;

import com.web.tilotoma.dto.*;
import com.web.tilotoma.entity.*;

import com.web.tilotoma.exceptions.ResourceNotFoundException;
import com.web.tilotoma.repository.ContractorRepository;
import com.web.tilotoma.repository.LabourAttendanceRepository;
import com.web.tilotoma.repository.LabourRepository;
import com.web.tilotoma.repository.LabourTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


public interface ContractorService {


    public Contractor addContractor(ContractorRequest req) ;

    // Get All Contractors
    public List<Contractor> getAllContractors();
    public List<ContractorResponse> getAllContractorsCustom();
    // Add Labour Under Contractor
    public Labour addLabourUnderContractor(Long contractorId, LabourRequest req);
    // Get All Labours
    public List<LabourResponse> getAllLabours();

    // Get All Labour Types
    public List<LabourType> getAllLabourTypes();

    // Add Labour Type
    public LabourType addLabourType(LabourTypeRequest req) ;
    public List<Labour> getLaboursByContractor(Long contractorId) ;


    public LabourAttendance markAttendance(Long labourId) ;

    public List<LabourAttendance> getAttendanceByLabour(Long labourId);
    public Project createProject(ProjectDto projectDto);
    public List<Project> getAllProjects() ;
    public Contractor getContractorById(Long contractorId);
    public Contractor updateContractorDetails(Long contractorId, ContractorRequest request);
    public void deleteContractorDetails(Long contractorId);

    //List<ContractorAttendanceReportDto> getContractorAttendanceReport(Long contractorId, LocalDate date);
    List<ContractorAttendanceReportDto> getContractorAttendanceReportBetweenDates(Long contractorId, LocalDate startDate, LocalDate endDate);
}
