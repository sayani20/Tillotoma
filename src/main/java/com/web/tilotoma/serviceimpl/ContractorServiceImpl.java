package com.web.tilotoma.serviceimpl;


import com.web.tilotoma.dto.*;
import com.web.tilotoma.entity.*;

import com.web.tilotoma.exceptions.ResourceNotFoundException;
import com.web.tilotoma.repository.*;
import com.web.tilotoma.service.ContractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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



    //create contractor
    public Contractor addContractor(ContractorRequest req) {
        // 1️⃣ Contractor create
        Contractor contractor = Contractor.builder()
                .contractorName(req.getContractorName())
                .username(req.getUserName())
                .email(req.getEmail())
                .address(req.getAddress())
                .mobileNumber(req.getMobileNumber())
                .isActive(true)
                .build();

        // 2️⃣ Contractor save
        Contractor savedContractor = contractorRepository.save(contractor);

        // 3️⃣ Multiple Project assign (if provided)
        if (req.getProjectIds() != null && !req.getProjectIds().isEmpty()) {
            List<Project> projects = projectRepo.findAllById(req.getProjectIds());

            if (projects.isEmpty()) {
                throw new RuntimeException("No projects found for given IDs: " + req.getProjectIds());
            }

            // প্রতিটা project এ contractor assign করো
            for (Project project : projects) {
                project.setContractor(savedContractor);
            }

            // একবারে সব project save করো
            projectRepo.saveAll(projects);
        }

        return savedContractor;
    }

    // Get All Contractors
    public List<ContractorResponse> getAllContractorsCustom() {
        List<Contractor> contractors = contractorRepository.findAll();

        return contractors.stream().map(contractor -> {
            long labourCount = contractor.getLabours() != null ? contractor.getLabours().size() : 0;

            long labourTypeCount = 0;
            if (contractor.getLabours() != null && !contractor.getLabours().isEmpty()) {
                labourTypeCount = contractor.getLabours().stream()
                        .filter(l -> l.getLabourType() != null)
                        .map(l -> l.getLabourType().getTypeName())
                        .distinct()
                        .count();
            }

            return ContractorResponse.builder()
                    .id(contractor.getId())
                    .contractorName(contractor.getContractorName())
                    .username(contractor.getUsername())
                    .email(contractor.getEmail())
                    .mobileNumber(contractor.getMobileNumber())
                    .isActive(contractor.getIsActive())
                    .createdOn(contractor.getCreatedOn())
                    .labourCount(labourCount)
                    .labourTypeCount(labourTypeCount)
                    .build();
        }).collect(Collectors.toList());
    }

    //get contractor details by id
    public Contractor getContractorById(Long contractorId) {
        return contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found with ID: " + contractorId));
    }

    //contractorId wise details update
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

    //delete contractor
    public void deleteContractorDetails(Long contractorId) {
        Contractor contractor = contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found with ID: " + contractorId));

        contractorRepository.delete(contractor);
    }










    //--------------------------Report Start-----------------------//

    // labour daily attendance mark
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

    //Labour Id Wise attendance report
    public List<LabourAttendance> getAttendanceByLabour(Long labourId) {
        if (!labourRepository.existsById(labourId)) {
            throw new RuntimeException("Labour not found");
        }
        return attendanceRepository.findByLabourIdOrderByAttendanceDateDesc(labourId);
    }

    // attendance report
    public List<ContractorAttendanceReportDto> getContractorAttendanceReportBetweenDates(Long contractorId, LocalDate startDate, LocalDate endDate) {
        Contractor contractor = contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found"));

        List<Labour> labours = labourRepository.findByContractorId(contractorId);
        List<ContractorAttendanceReportDto> reportList = new ArrayList<>();

        for (Labour labour : labours) {
            List<LabourAttendance> attendances =
                    attendanceRepository.findByLabourIdAndAttendanceDateBetween(
                            labour.getId(), startDate, endDate);

            if (attendances.isEmpty()) continue;

            for (LabourAttendance attendance : attendances) {
                ContractorAttendanceReportDto dto = ContractorAttendanceReportDto.builder()
                        .labourId(labour.getId())
                        .labourName(labour.getLabourName())
                        .labourType(
                                ContractorAttendanceReportDto.LabourTypeDto.builder()
                                        .id(labour.getLabourType().getId())
                                        .typeName(labour.getLabourType().getTypeName())
                                        .build()
                        )
                        .projects(
                                labour.getProjects().stream()
                                        .map(p -> ContractorAttendanceReportDto.ProjectDto.builder()
                                                .id(p.getId())
                                                .name(p.getName())
                                                .build())
                                        .toList()
                        )
                        .attendance(
                                ContractorAttendanceReportDto.AttendanceDto.builder()
                                        .attendanceDate(attendance.getAttendanceDate())
                                        .inTime(attendance.getInTime())
                                        .outTime(attendance.getOutTime())
                                        .isPresent(attendance.getIsPresent())
                                        .build()
                        )
                        .build();

                reportList.add(dto);
            }
        }

        return reportList;
    }
    //--------------------------Report END-----------------------//


}
