package com.web.tilotoma.service;

import com.web.tilotoma.dto.ContractorRequest;
import com.web.tilotoma.dto.LabourRequest;
import com.web.tilotoma.dto.LabourTypeRequest;
import com.web.tilotoma.entity.Contractor;
import com.web.tilotoma.entity.Labour;
import com.web.tilotoma.entity.LabourAttendance;
import com.web.tilotoma.entity.LabourType;

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

@Service
public interface ContractorService {


    public Contractor addContractor(ContractorRequest req) ;

    // Get All Contractors
    public List<Contractor> getAllContractors();

    // Add Labour Under Contractor
    public Labour addLabourUnderContractor(Long contractorId, LabourRequest req);
    // Get All Labours
    public List<Labour> getAllLabours();

    // Get All Labour Types
    public List<LabourType> getAllLabourTypes();

    // Add Labour Type
    public LabourType addLabourType(LabourTypeRequest req) ;
    public List<Labour> getLaboursByContractor(Long contractorId) ;


    public LabourAttendance markAttendance(Long labourId) ;

    public List<LabourAttendance> getAttendanceByLabour(Long labourId);
}
