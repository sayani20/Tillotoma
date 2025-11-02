package com.web.tilotoma.serviceimpl;


import com.web.tilotoma.dto.ContractorRequest;
import com.web.tilotoma.dto.LabourRequest;
import com.web.tilotoma.dto.LabourTypeRequest;
import com.web.tilotoma.dto.ProjectDto;
import com.web.tilotoma.entity.*;

import com.web.tilotoma.exceptions.ResourceNotFoundException;
import com.web.tilotoma.repository.*;
import com.web.tilotoma.service.ContractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContractorServiceImpl implements ContractorService {
    @Autowired
    private ContractorRepository contractorRepository;

    @Autowired
    private LabourRepository labourRepository;

    @Autowired
    private LabourTypeRepository labourTypeRepository;

    @Autowired
    private LabourAttendanceRepository attendanceRepository;
    @Autowired
    private ProjectRepo projectRepo;

    public Contractor addContractor(ContractorRequest req) {
        //Contractor  create
        Contractor contractor = Contractor.builder()
                .contractorName(req.getContractorName())
                .username(req.getUserName())
                .email(req.getEmail())
                .address(req.getAddress())
                .mobileNumber(req.getMobileNumber())
                .isActive(true)
                .build();

        // Contractor save
        Contractor savedContractor = contractorRepository.save(contractor);

        //  projectId assign
        if (req.getProjectId() != null) {
            Project project = projectRepo.findById(req.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found with ID: " + req.getProjectId()));

            // contractor assign
            project.setContractor(savedContractor);
            projectRepo.save(project);
        }

        return savedContractor;
    }

    // Get All Contractors
    public List<Contractor> getAllContractors() {
        return contractorRepository.findAll();
    }

    // Add Labour Under Contractor
    public Labour addLabourUnderContractor(Long contractorId, LabourRequest req) {
        Contractor contractor = contractorRepository.findById(contractorId)
                .orElseThrow(() -> new ResourceNotFoundException("Contractor not found"));

        LabourType labourType = labourTypeRepository.findById(req.getLabourTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Labour type not found"));

        Labour labour = Labour.builder()
                .labourName(req.getLabourName())
                .email(req.getEmail())
                .mobileNumber(req.getMobileNumber())
                .labourType(labourType)
                .contractor(contractor)
                .build();

        return labourRepository.save(labour);
    }

    // Get All Labours
    public List<Labour> getAllLabours() {
        return labourRepository.findAll();
    }

    // Get All Labour Types
    public List<LabourType> getAllLabourTypes() {
        return labourTypeRepository.findAll();
    }

    // Add Labour Type
    public LabourType addLabourType(LabourTypeRequest req) {
        LabourType labourType = LabourType.builder()
                .typeName(req.getTypeName())
                //.dailyRate(req.getDailyRate())
                //.hourlyRate(req.getHourlyRate())
                .build();

        return labourTypeRepository.save(labourType);
    }

    public List<Labour> getLaboursByContractor(Long contractorId) {
        // optional: check contractor exists or not
        contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found"));

        return labourRepository.findByContractorId(contractorId);
    }


    public LabourAttendance markAttendance(Long labourId) {
        Labour labour = labourRepository.findById(labourId)
                .orElseThrow(() -> new RuntimeException("Labour not found"));

        LocalDate today = LocalDate.now();

        Optional<LabourAttendance> existingAttendance =
                attendanceRepository.findByLabourIdAndAttendanceDate(labourId, today);

        if (existingAttendance.isEmpty()) {
            // ✅ First time hit — mark In-Time
            LabourAttendance attendance = LabourAttendance.builder()
                    .labour(labour)
                    .attendanceDate(today)
                    .inTime(LocalTime.now())
                    .isPresent(true)
                    .build();
            return attendanceRepository.save(attendance);
        }

        LabourAttendance attendance = existingAttendance.get();

        if (attendance.getOutTime() == null) {
            // ✅ Second hit — mark Out-Time
            attendance.setOutTime(LocalTime.now());
            return attendanceRepository.save(attendance);
        } else {
            // ⚠️ Already marked both times
            throw new RuntimeException("Attendance already completed for today");
        }
    }

    public List<LabourAttendance> getAttendanceByLabour(Long labourId) {
        if (!labourRepository.existsById(labourId)) {
            throw new RuntimeException("Labour not found");
        }
        return attendanceRepository.findByLabourIdOrderByAttendanceDateDesc(labourId);
    }

    public Project createProject(ProjectDto projectDto) {
        Project project = Project.builder()
                .name(projectDto.getName())
                .description(projectDto.getDescription())
                .location(projectDto.getLocation())
                .city(projectDto.getCity())
                .state(projectDto.getState())
                .pincode(projectDto.getPincode())
                .landArea(projectDto.getLandArea())
                .builtUpArea(projectDto.getBuiltUpArea())
                .startDate(projectDto.getStartDate())
                .endDate(projectDto.getEndDate())
                .possessionDate(projectDto.getPossessionDate())
                .projectType(projectDto.getProjectType())
                .projectStatus(projectDto.getProjectStatus())
                .totalNumberOfFlats(projectDto.getTotalNumberOfFlats())
                .totalNumberOfFloors(projectDto.getTotalNumberOfFloors())
                .budgetEstimate(projectDto.getBudgetEstimate())
                .remarks(projectDto.getRemarks())
                .approvedByAuthority(projectDto.getApprovedByAuthority())
                .approvalNumber(projectDto.getApprovalNumber())
                .createdBy(projectDto.getCreatedBy())
                .isActive(true)
                .build();
        if (projectDto.getContractorId() != null) {
            Contractor contractor = contractorRepository.findById(projectDto.getContractorId())
                    .orElseThrow(() -> new RuntimeException("Contractor not found"));
            project.setContractor(contractor);
        }

        return projectRepo.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepo.findAll();
    }
    public Contractor getContractorById(Long contractorId) {
        return contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found with ID: " + contractorId));
    }

    public Contractor updateContractorDetails(Long contractorId, ContractorRequest request) {
        Contractor contractor = contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found with ID: " + contractorId));
        contractor.setContractorName(request.getContractorName() != null ? request.getContractorName() : contractor.getContractorName());
        contractor.setUsername(request.getUserName() != null ? request.getUserName() : contractor.getUsername());
        contractor.setEmail(request.getEmail() != null ? request.getEmail() : contractor.getEmail());
        contractor.setMobileNumber(request.getMobileNumber() != null ? request.getMobileNumber() : contractor.getMobileNumber());
        contractor.setAddress(request.getAddress() != null ? request.getAddress() : contractor.getAddress());

        return contractorRepository.save(contractor);
    }
    public void deleteContractorDetails(Long contractorId) {
        Contractor contractor = contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found with ID: " + contractorId));

        contractorRepository.delete(contractor);
    }



}
