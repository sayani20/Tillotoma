package com.web.tilotoma.serviceimpl;


import com.web.tilotoma.dto.*;
import com.web.tilotoma.dto.response.ContractorDetailsDto;
import com.web.tilotoma.entity.*;

import com.web.tilotoma.exceptions.ResourceNotFoundException;
import com.web.tilotoma.repository.*;
import com.web.tilotoma.service.ContractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @Autowired
    ContractorLabourRateRepository contractorLabourRateRepository;



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
    public ContractorDetailsDto getContractorById(Long contractorId) {
        Contractor contractor = contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found with ID: " + contractorId));

        return ContractorDetailsDto.builder()
                .id(contractor.getId())
                .contractorName(contractor.getContractorName())
                .username(contractor.getUsername())
                .email(contractor.getEmail())
                .mobileNumber(contractor.getMobileNumber())
                .address(contractor.getAddress())
                .isActive(contractor.getIsActive())
                .createdOn(contractor.getCreatedOn())
                .labours(contractor.getLabours() != null
                        ? contractor.getLabours().stream()
                        .map(l -> ContractorDetailsDto.LabourDto.builder()
                                .id(l.getId())
                                .labourName(l.getLabourName())
                                .email(l.getEmail())
                                .mobileNumber(l.getMobileNumber())
                                .labourTypeId(l.getLabourType().getId())
                                .labourTypeName(l.getLabourType().getTypeName())
                                .isActive(l.getIsActive())
                                .createdOn(l.getCreatedOn())
                                .build())
                        .toList()
                        : null)
                .build();
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

    public List<ContractorProjectMonthlyReportDto> getMonthlyProjectReport(Long contractorId, int year, int month) {
        Contractor contractor = contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found"));

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Labour> labours = labourRepository.findByContractor(contractor);

        Map<String, List<Labour>> projectGroups = labours.stream()
                .filter(l -> l.getProjects() != null)
                .flatMap(l -> l.getProjects().stream().map(p -> Map.entry(p.getName(), l)))
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        List<ContractorProjectMonthlyReportDto> reports = new ArrayList<>();

        for (Map.Entry<String, List<Labour>> entry : projectGroups.entrySet()) {
            String projectName = entry.getKey();
            List<Labour> projectLabours = entry.getValue();

            long totalLabour = projectLabours.size();
            long totalWorkingDays = 0;
            double totalHours = 0;
            double totalAmount = 0;

            for (Labour labour : projectLabours) {
                List<LabourAttendance> attendances =
                        attendanceRepository.findByLabourIdAndAttendanceDateBetween(
                                labour.getId(), startDate, endDate);

                long daysWorked = attendances.stream()
                        .filter(LabourAttendance::getIsPresent)
                        .count();

                double totalMinutesWorked = attendances.stream()
                        .filter(a -> a.getInTime() != null && a.getOutTime() != null)
                        .mapToDouble(a -> ChronoUnit.MINUTES.between(a.getInTime(), a.getOutTime()))
                        .sum();

                // convert minutes to hours (for report only)
                double hoursWorked = totalMinutesWorked / 60.0;

                totalWorkingDays += daysWorked;
                totalHours += hoursWorked;

                double dailyRate = contractorLabourRateRepository
                        .findByContractorAndLabourType(contractor, labour.getLabourType())
                        .map(ContractorLabourRate::getDailyRate)
                        .orElse(0.0);

                // ✅ Standard 8 hours per day
                double perHourRate = dailyRate / 8.0;
                double perMinuteRate = perHourRate / 60.0;

                // ✅ Total amount = per minute × total minutes worked
                totalAmount += totalMinutesWorked * perMinuteRate;
            }

            totalHours = Math.round(totalHours * 100.0) / 100.0;
            totalAmount = Math.round(totalAmount * 100.0) / 100.0;

            reports.add(ContractorProjectMonthlyReportDto.builder()
                    .contractorName(contractor.getContractorName())
                    .projectName(projectName)
                    .month(startDate.getMonth() + " " + year)
                    .totalLabour(totalLabour)
                    .totalWorkingDays(totalWorkingDays)
                    .totalHours(totalHours)
                    .totalAmount(totalAmount)
                    .build());
        }

        return reports;
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
