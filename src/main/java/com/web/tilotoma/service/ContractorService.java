package com.web.tilotoma.service;

import com.web.tilotoma.dto.*;
import com.web.tilotoma.dto.response.ContractorDetailsDto;
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
    public List<ContractorResponse> getAllContractorsCustom();







    public LabourAttendance markAttendance(Long labourId) ;

    public List<LabourAttendance> getAttendanceByLabour(Long labourId);

    public ContractorDetailsDto getContractorById(Long contractorId);
    public Contractor updateContractorDetails(Long contractorId, ContractorRequest request);
    public void deleteContractorDetails(Long contractorId);

    //List<ContractorAttendanceReportDto> getContractorAttendanceReport(Long contractorId, LocalDate date);
    List<ContractorAttendanceReportDto> getContractorAttendanceReportBetweenDates(Long contractorId, LocalDate startDate, LocalDate endDate);
}
