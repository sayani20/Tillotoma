package com.web.tilotoma.serviceimpl;


import com.web.tilotoma.dto.*;

import com.web.tilotoma.dto.bill.ContractorBillingResponse;

import com.web.tilotoma.dto.bill.LabourBillingDetailsResponse;
import com.web.tilotoma.dto.response.ContractorDetailsDto;
import com.web.tilotoma.dto.response.LabourBillingDto;
import com.web.tilotoma.dto.response.ProjectBillingDto;
import com.web.tilotoma.entity.*;


import com.web.tilotoma.repository.*;
import com.web.tilotoma.service.ContractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
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

        // 0️⃣ **Project mandatory validation**
        if (req.getProjectIds() == null || req.getProjectIds().isEmpty()) {
            throw new RuntimeException("Project type is required. Please select at least one project.");
        }

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
        List<Project> projects = projectRepo.findAllById(req.getProjectIds());

        if (projects.isEmpty()) {
            throw new RuntimeException("No projects found for given IDs: " + req.getProjectIds());
        }

        // Assign contractor to each project
        for (Project project : projects) {
            project.setContractor(savedContractor);
        }

        // Save all updated projects
        projectRepo.saveAll(projects);

        return savedContractor;
    }


    @Transactional(readOnly = true)
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

            // Map projects to ProjectSimpleResponse, guard null
            List<ProjectSimpleResponse> projectResponses = contractor.getProjects() != null
                    ? contractor.getProjects().stream()
                    .map(p -> ProjectSimpleResponse.builder()
                            .id(p.getId())
                            .name(p.getName())
                            .location(p.getLocation())
                            .city(p.getCity())
                            .state(p.getState())
                            .isActive(p.getIsActive())
                            .build())
                    .collect(Collectors.toList())
                    : Collections.emptyList();

            return ContractorResponse.builder()
                    .id(contractor.getId())
                    .contractorName(contractor.getContractorName())
                    .username(contractor.getUsername())
                    .email(contractor.getEmail())
                    .mobileNumber(contractor.getMobileNumber())
                    .isActive(contractor.getIsActive())
                    .createdOn(contractor.getCreatedOn())
                    .address(contractor.getAddress())
                    .labourCount(labourCount)
                    .labourTypeCount(labourTypeCount)
                    .projects(projectResponses)   // <-- new
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

    @Override
    public Contractor updateContractorDetails(Long contractorId, ContractorRequest request) {

        Contractor contractor = contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found with ID: " + contractorId));

        // Update basic details
        contractor.setContractorName(
                request.getContractorName() != null ? request.getContractorName() : contractor.getContractorName());
        contractor.setUsername(
                request.getUserName() != null ? request.getUserName() : contractor.getUsername());
        contractor.setEmail(
                request.getEmail() != null ? request.getEmail() : contractor.getEmail());
        contractor.setMobileNumber(
                request.getMobileNumber() != null ? request.getMobileNumber() : contractor.getMobileNumber());
        contractor.setAddress(
                request.getAddress() != null ? request.getAddress() : contractor.getAddress());

        // ----------------------------------------------
        // 🔥 PROJECT UPDATE LOGIC (THIS WAS MISSING)
        // ----------------------------------------------
        if (request.getProjectIds() != null) {

            // আগে পুরানো contractor এর সব project unlink করে দিই
            List<Project> oldProjects = contractor.getProjects();
            if (oldProjects != null) {
                for (Project p : oldProjects) {
                    p.setContractor(null);
                }
            }

            // new project assign
            List<Project> newProjects = projectRepo.findAllById(request.getProjectIds());
            for (Project p : newProjects) {
                p.setContractor(contractor);
            }

            contractor.setProjects(newProjects);
        }

        // finally save
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
    /*public List<ContractorAttendanceReportDto> getContractorAttendanceReportBetweenDates(Long contractorId, LocalDate startDate, LocalDate endDate) {
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
                        .labourUserId(labour.getLabourUserId())
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
                                        .isCheck(attendance.getIsCheck())
                                        .build()
                        )
                        .build();

                reportList.add(dto);
            }
        }

        return reportList;
    }*/

    public List<ContractorAttendanceReportDto> getContractorAttendanceReportBetweenDates(
            Long contractorId, LocalDate startDate, LocalDate endDate) {

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

                Long durationMinutes = 0L;
                Boolean computedIsCheck = false;

                LocalTime inTime = attendance.getInTime();
                LocalTime outTime = attendance.getOutTime();

                if (inTime != null && outTime != null && !outTime.isBefore(inTime)) {
                    Duration duration = Duration.between(inTime, outTime);
                    durationMinutes = duration.toMinutes();

                    // > 7 hours
                    computedIsCheck = durationMinutes > (7 * 60);
                }

                // ⬅️ DB তে isCheck সেভ করে দিচ্ছি
                attendance.setIsCheck(computedIsCheck);
                attendanceRepository.save(attendance);

                ContractorAttendanceReportDto dto = ContractorAttendanceReportDto.builder()
                        .labourId(labour.getId())
                        .labourName(labour.getLabourName())
                        .labourUserId(labour.getLabourUserId())
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
                                        .inTime(inTime)
                                        .outTime(outTime)
                                        .isPresent(attendance.getIsPresent())
                                        .isCheck(computedIsCheck)
                                        .durationMinutes(durationMinutes)
                                        .build()
                        )
                        .build();

                reportList.add(dto);
            }
        }

        return reportList;
    }


    public String updateIsCheck(Long labourId, LocalDate attendanceDate, Boolean isCheck) {

        Labour labour = labourRepository.findById(labourId)
                .orElseThrow(() -> new RuntimeException("Labour not found"));

        LabourAttendance attendance =
                attendanceRepository.findByLabourIdAndAttendanceDate(labourId, attendanceDate)
                        .orElseThrow(() -> new RuntimeException("Attendance record not found"));

        attendance.setIsCheck(isCheck);
        attendanceRepository.save(attendance);

        return "isCheck updated successfully";
    }


    //--------------------------Report END-----------------------//


    /*@Override
    public ApiResponse<List<ContractorBillingResponse>> getBillingReport(LocalDate fromDate, LocalDate toDate) {

        List<ContractorBillingResponse> billingList = new ArrayList<>();
        List<Contractor> contractors = contractorRepository.findAll();

        System.out.println("==== BILLING REPORT START ====");
        System.out.println("From: " + fromDate + " | To: " + toDate);

        for (Contractor contractor : contractors) {
            System.out.println("\nContractor: " + contractor.getContractorName() + " (ID: " + contractor.getId() + ")");


            List<Project> projects = projectRepo.findProjectsByLabourContractor(contractor);
            System.out.println("  Total Projects Found: " + projects.size());

            for (Project project : projects) {
                System.out.println("  -> Project: " + project.getName() + " (ID: " + project.getId() + ")");


                List<Labour> labours = labourRepository.findByProjects(project);
                System.out.println("     Total Labours: " + labours.size());

                double totalProjectHours = 0.0;
                double totalProjectAmount = 0.0;

                for (Labour labour : labours) {
                    List<LabourAttendance> attendances =
                            attendanceRepository.findByLabourAndAttendanceDateBetween(labour, fromDate, toDate);

                    double totalHoursForLabour = attendances.stream()
                            .mapToDouble(a -> {
                                if (a.getInTime() != null && a.getOutTime() != null) {
                                    Duration duration = Duration.between(a.getInTime(), a.getOutTime());
                                    return duration.toMinutes() / 60.0;
                                }
                                return 0.0;
                            })
                            .sum();

                    double amountForLabour = 0.0;
                    if (labour.getRatePerDay() != null && labour.getRatePerDay() > 0) {
                        amountForLabour = (labour.getRatePerDay() / 7.0) * totalHoursForLabour;
                    }

                    totalProjectHours += totalHoursForLabour;
                    totalProjectAmount += amountForLabour;

                    System.out.println("       Labour: " + labour.getLabourName() + " | Hours: "
                            + totalHoursForLabour + " | Amount: " + amountForLabour);
                }

                ContractorBillingResponse response = new ContractorBillingResponse();
                response.setContractorId(contractor.getId());
                response.setContractorName(contractor.getContractorName());
                response.setContractorEmail(contractor.getEmail());
                response.setContractorMobile(contractor.getMobileNumber());
                response.setTotalHours(totalProjectHours);
                response.setTotalAmount(totalProjectAmount);
                response.setProjectId(project.getId());
                response.setProjectName(project.getName());
                response.setTotalLabours(labours.size());

                billingList.add(response);

                System.out.println("  Project Summary -> Hours: " + totalProjectHours + ", Amount: " + totalProjectAmount);
            }
        }

        System.out.println("\n==== BILLING REPORT END ====");

        return new ApiResponse<>(true, "Billing report fetched successfully", billingList);
    }*/

    @Override
    public ApiResponse<List<ContractorBillingResponse>> getBillingReport(LocalDate fromDate, LocalDate toDate) {

        List<ContractorBillingResponse> billingList = new ArrayList<>();
        List<Contractor> contractors = contractorRepository.findAll();

        System.out.println("==== BILLING REPORT START ====");
        System.out.println("From: " + fromDate + " | To: " + toDate);

        for (Contractor contractor : contractors) {
            System.out.println("\nContractor: " + contractor.getContractorName() + " (ID: " + contractor.getId() + ")");

            List<Project> projects = projectRepo.findProjectsByLabourContractor(contractor);
            System.out.println("  Total Projects Found: " + projects.size());

            for (Project project : projects) {
                System.out.println("  -> Project: " + project.getName() + " (ID: " + project.getId() + ")");

                List<Labour> labours = labourRepository.findByProjects(project);
                System.out.println("     Total Labours: " + labours.size());

                double totalProjectHours = 0.0;
                double totalProjectAmount = 0.0;

                for (Labour labour : labours) {
                    // get all attendances in date range for this labour
                    List<LabourAttendance> attendances =
                            attendanceRepository.findByLabourAndAttendanceDateBetween(labour, fromDate, toDate);

                    // filter only those attendances where isCheck == true
                    List<LabourAttendance> checkedAttendances = attendances.stream()
                            .filter(a -> Boolean.TRUE.equals(a.getIsCheck()))
                            .toList();

                    // if no checked attendances, skip calculations for this labour
                    if (checkedAttendances.isEmpty()) {
                        System.out.println("       Labour: " + labour.getLabourName() + " | No checked attendances -> skipped");
                        continue;
                    }

                    double totalHoursForLabour = checkedAttendances.stream()
                            .mapToDouble(a -> {
                                if (a.getInTime() != null && a.getOutTime() != null && !a.getOutTime().isBefore(a.getInTime())) {
                                    Duration duration = Duration.between(a.getInTime(), a.getOutTime());
                                    return duration.toMinutes() / 60.0;
                                }
                                return 0.0;
                            })
                            .sum();

                    double amountForLabour = 0.0;
                    if (labour.getRatePerDay() != null && labour.getRatePerDay() > 0) {
                        // same logic as before: (ratePerDay / 7) * hours
                        amountForLabour = (labour.getRatePerDay() / 7.0) * totalHoursForLabour;
                    }

                    totalProjectHours += totalHoursForLabour;
                    totalProjectAmount += amountForLabour;

                    System.out.println("       Labour: " + labour.getLabourName() + " | Checked Hours: "
                            + totalHoursForLabour + " | Amount: " + amountForLabour);
                }

                // Only add project summary if there was some billing (optional)
                ContractorBillingResponse response = new ContractorBillingResponse();
                response.setContractorId(contractor.getId());
                response.setContractorName(contractor.getContractorName());
                response.setContractorEmail(contractor.getEmail());
                response.setContractorMobile(contractor.getMobileNumber());
                response.setTotalHours(totalProjectHours);
                response.setTotalAmount(totalProjectAmount);
                response.setProjectId(project.getId());
                response.setProjectName(project.getName());
                response.setTotalLabours(labours.size());

                billingList.add(response);

                System.out.println("  Project Summary -> Checked Hours: " + totalProjectHours + ", Amount: " + totalProjectAmount);
            }
        }

        System.out.println("\n==== BILLING REPORT END ====");

        return new ApiResponse<>(true, "Billing report fetched successfully", billingList);
    }



    @Override
    public List<LabourBillingDetailsResponse> getLabourBillingDetails(
            Long contractorId, Long projectId, LocalDate fromDate, LocalDate toDate) {

        Contractor contractor = contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found"));

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // all labours assigned to this project
        List<Labour> labours = labourRepository.findByProjects(project);

        List<LabourBillingDetailsResponse> responseList = new ArrayList<>();

        for (Labour labour : labours) {
            // filter by same contractor just to be safe
            if (!labour.getContractor().getId().equals(contractorId)) continue;

            List<LabourAttendance> attendances = attendanceRepository
                    .findByLabourAndAttendanceDateBetween(labour, fromDate, toDate);

            long totalDays = attendances.stream()
                    .filter(LabourAttendance::getIsPresent)
                    .count();

            double totalHours = attendances.stream()
                    .mapToDouble(a -> {
                        if (a.getInTime() != null && a.getOutTime() != null) {
                            return Duration.between(a.getInTime(), a.getOutTime()).toMinutes() / 60.0;
                        }
                        return 0.0;
                    })
                    .sum();

            Double ratePerDay = labour.getRatePerDay() != null ? labour.getRatePerDay() : 0.0;
            Double billAmount = (ratePerDay / 7.0) * totalHours;

            LabourBillingDetailsResponse dto = LabourBillingDetailsResponse.builder()
                    .labourId(labour.getId())
                    .labourName(labour.getLabourName())
                    .labourType(labour.getLabourType().getTypeName())
                    .totalDays(totalDays)
                    .totalHours(round(totalHours))
                    .ratePerDay(ratePerDay)
                    .billAmount(round(billAmount))
                    .build();

            responseList.add(dto);
        }

        return responseList;
    }


    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

}
