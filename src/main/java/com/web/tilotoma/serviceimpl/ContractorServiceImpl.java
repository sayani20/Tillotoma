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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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


    @Autowired
    ContractorPaymentRepository contractorPaymentRepository;

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


  /*  //delete contractor
    public void deleteContractorDetails(Long contractorId) {
        Contractor contractor = contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found with ID: " + contractorId));

        contractorRepository.delete(contractor);
    }*/

    @Transactional
    public void deleteContractorDetails(Long contractorId) {

        Contractor contractor = contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found with ID: " + contractorId));

        // Step 1: Remove contractor reference from projects
        List<Project> projects = projectRepo.findProjectsByLabourContractor(contractor);
        for (Project project : projects) {
            project.setContractor(null);
        }
        projectRepo.saveAll(projects);

        // Step 2: Remove contractor reference from labours
        List<Labour> labours = labourRepository.findByContractor(contractor);
        for (Labour labour : labours) {
            labour.setContractor(null);
        }
        labourRepository.saveAll(labours);

        // Step 3: Now safely delete contractor
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
                    // isCheck ডিফল্টভাবে true রাখছি; চাহিলে এখানে null বা true/false সেট করতে পারো
                    .isCheck(false)
                    .build();
            return attendanceRepository.save(attendance);
        }

        LabourAttendance attendance = existingAttendance.get();

        if (attendance.getOutTime() == null) {
            // ✅ Second hit — mark Out-Time
            LocalTime out = LocalTime.now();
            attendance.setOutTime(out);

            // compute duration between inTime and outTime (handle crossing-midnight)
            LocalTime in = attendance.getInTime();
            if (in == null) {
                // safety: যদি inTime নেই, তখন শুধু save করে দেওয়া যেতে পারে বা exception দিতে
                throw new RuntimeException("In time not recorded for today");
            }

            LocalDateTime inDateTime = LocalDateTime.of(attendance.getAttendanceDate(), in);
            LocalDateTime outDateTime = LocalDateTime.of(attendance.getAttendanceDate(), out);

            // যদি outTime, inTime এর আগে পড়ে ধরে নেই এটা পরের দিনের আউট
            if (out.isBefore(in)) {
                outDateTime = outDateTime.plusDays(1);
            }

            Duration worked = Duration.between(inDateTime, outDateTime);

            // যদি কাজের সময় 7 ঘণ্টার কম হয় => isCheck = false, নাহলে true
            boolean check = !worked.minusHours(8).isNegative() || worked.toMinutes() >= 7 * 60;
            // সরলভাবে: worked >= 7 hours -> true, else false
            attendance.setIsCheck(check);

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
                Double todayBillAmount = 0.0;

                LocalTime inTime = attendance.getInTime();
                LocalTime outTime = attendance.getOutTime();

                if (inTime != null && outTime != null && !outTime.isBefore(inTime)) {

                    Duration duration = Duration.between(inTime, outTime);
                    durationMinutes = duration.toMinutes();
                    double hours = durationMinutes / 60.0;

                    double ratePerDay = labour.getRatePerDay() != null ? labour.getRatePerDay() : 0.0;

                    // ---------------- isCheck Logic ----------------
                    if ((hours >= 4 && hours <= 5) ||
                            (hours >= 8 && hours <= 9) ||
                            (hours >= 11 && hours <= 12) ||
                            (hours >= 14 && hours <= 15)) {

                        computedIsCheck = true;
                    } else {
                        computedIsCheck = false;
                    }

                    // ---------------- Billing Logic ----------------
                    if (hours >= 4 && hours <= 5) {
                        todayBillAmount = ratePerDay / 2;
                    }
                    else if (hours >= 8 && hours <= 9) {
                        todayBillAmount = ratePerDay;
                    }
                    else if (hours >= 11 && hours <= 12) {
                        todayBillAmount = ratePerDay * 1.5;
                    }
                    else if (hours >= 14 && hours <= 15) {
                        todayBillAmount = ratePerDay * 2;
                    }
                    else {
                        // Pro-rata calculation
                        todayBillAmount = (ratePerDay / 8.0) * hours;
                    }

                    // ---------------- Save to DB (optional) ----------------
                    // attendance.setIsCheck(computedIsCheck);
                    // attendanceRepository.save(attendance);
                }

                ContractorAttendanceReportDto dto = ContractorAttendanceReportDto.builder()
                        .labourId(labour.getId())
                        .labourName(labour.getLabourName())
                        .labourUserId(labour.getLabourUserId())
                        .ratePerDay(labour.getRatePerDay())
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
                                        .todayBillAmount(todayBillAmount)
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



   /* @Override
    public ApiResponse<List<ContractorBillingResponse>> getBillingReport(LocalDate fromDate, LocalDate toDate) {

        List<ContractorBillingResponse> billingList = new ArrayList<>();
        List<Contractor> contractors = contractorRepository.findAll();

        System.out.println("==== BILLING REPORT START ====");
        System.out.println("From: " + fromDate + " | To: " + toDate);

        for (Contractor contractor : contractors) {

            System.out.println("\nContractor: " + contractor.getContractorName());

            // ✅ STEP–1: Get projects for this contractor THROUGH LABOURS
            List<Project> projects = projectRepo.findProjectsByContractorViaLabours(contractor);
            System.out.println("  Total Projects: " + projects.size());

            for (Project project : projects) {

                System.out.println("  -> Project: " + project.getName());

                // ✅ STEP–2: Get labours of this contractor who worked on this project
                List<Labour> labours =
                        labourRepository.findByContractorAndProjects(contractor, project);

                System.out.println("     Total Labours for this contractor: " + labours.size());

                double totalProjectHours = 0.0;
                double totalProjectAmount = 0.0;

                for (Labour labour : labours) {

                    List<LabourAttendance> attendances =
                            attendanceRepository.findByLabourAndAttendanceDateBetween(labour, fromDate, toDate);

                    List<LabourAttendance> checkedAttendances = attendances.stream()
                            .filter(a -> Boolean.TRUE.equals(a.getIsCheck()))
                            .toList();

                    if (checkedAttendances.isEmpty()) {
                        System.out.println("       Labour: " + labour.getLabourName() +
                                " | No checked attendances -> skipped");
                        continue;
                    }

                    double totalHoursForLabour = checkedAttendances.stream()
                            .mapToDouble(a -> {
                                if (a.getInTime() != null && a.getOutTime() != null &&
                                        !a.getOutTime().isBefore(a.getInTime())) {
                                    Duration duration = Duration.between(a.getInTime(), a.getOutTime());
                                    return duration.toMinutes() / 60.0;
                                }
                                return 0.0;
                            })
                            .sum();

                    double amountForLabour = 0.0;
                    Double ratePerDay = labour.getRatePerDay();

                    if (ratePerDay != null && ratePerDay > 0 && totalHoursForLabour > 0) {
                        double h = totalHoursForLabour;

                        if (h >= 4.0 && h <= 5.0) {
                            amountForLabour = ratePerDay / 2.0;
                        } else if (h >= 8.0 && h <= 9.0) {
                            amountForLabour = ratePerDay;
                        } else if (h >= 11.0 && h <= 12.0) {
                            amountForLabour = ratePerDay * 1.5;
                        } else if (h >= 14.0 && h <= 15.0) {
                            amountForLabour = ratePerDay * 2.0;
                        } else {
                            amountForLabour = (ratePerDay / 8.0) * h; // Hourly fallback
                        }
                    }

                    totalProjectHours += totalHoursForLabour;
                    totalProjectAmount += amountForLabour;

                    System.out.println("       Labour: " + labour.getLabourName() +
                            " | Hours: " + totalHoursForLabour +
                            " | Amount: " + amountForLabour);
                }

                if (totalProjectHours == 0.0 && totalProjectAmount == 0.0) {
                    System.out.println("  -> Skipped project (no attendance)");
                    continue;
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
    }
*/


    @Override
    public ApiResponse<List<ContractorBillingResponse>> getBillingReport(LocalDate fromDate, LocalDate toDate) {

        List<ContractorBillingResponse> billingList = new ArrayList<>();
        List<Contractor> contractors = contractorRepository.findAll();

        List<LocalDate> attendanceDates = fromDate.datesUntil(toDate.plusDays(1)).toList();

        for (LocalDate billDate : attendanceDates) {

            for (Contractor contractor : contractors) {

                List<LabourAttendance> dayAttendance =
                        attendanceRepository.findByLabour_Contractor_IdAndAttendanceDateBetween(
                                contractor.getId(), billDate, billDate);

                if (dayAttendance.isEmpty()) continue;

                Map<Project, List<LabourAttendance>> projectWiseMap = new HashMap<>();

                for (LabourAttendance att : dayAttendance) {
                    if (!Boolean.TRUE.equals(att.getIsCheck())) continue;

                    for (Project p : att.getLabour().getProjects()) {
                        projectWiseMap.computeIfAbsent(p, k -> new ArrayList<>()).add(att);
                    }
                }

                for (Map.Entry<Project, List<LabourAttendance>> entry : projectWiseMap.entrySet()) {

                    Project project = entry.getKey();
                    List<LabourAttendance> attendanceList = entry.getValue();

                    double totalHours = 0;
                    double totalAmount = 0;

                    for (LabourAttendance att : attendanceList) {
                        Labour labour = att.getLabour();

                        if (att.getInTime() != null && att.getOutTime() != null &&
                                !att.getOutTime().isBefore(att.getInTime())) {

                            Duration d = Duration.between(att.getInTime(), att.getOutTime());
                            double hrs = d.toMinutes() / 60.0;
                            totalHours += hrs;

                            Double ratePerDay = labour.getRatePerDay();
                            double amount = 0;

                            if (ratePerDay != null) {
                                if (hrs >= 4 && hrs <= 5) amount = ratePerDay / 2.0;
                                else if (hrs >= 8 && hrs <= 9) amount = ratePerDay;
                                else if (hrs >= 11 && hrs <= 12) amount = ratePerDay * 1.5;
                                else if (hrs >= 14 && hrs <= 15) amount = ratePerDay * 2.0;
                                else amount = (ratePerDay / 8.0) * hrs;
                            }
                            totalAmount += amount;
                        }
                    }

                    if (totalHours == 0) continue;

                    // 🔥 BILL NO GENERATION
                    String prefix = contractor.getContractorName().substring(0, 2).toUpperCase();
                    String billNo = prefix + billDate.format(DateTimeFormatter.ofPattern("ddMMyyyy"));

                    // 🔥 FETCH PAYMENT (IF EXISTS)
                    Optional<ContractorPayment> paymentOpt =
                            contractorPaymentRepository.findByContractorIdAndBillNo(contractor.getId(), billNo);

                    Double paidAmount = 0.0;
                    String billStatus = "NOT_PAID";

                    if (paymentOpt.isPresent()) {
                        ContractorPayment p = paymentOpt.get();
                        paidAmount = p.getPaidAmount();
                        billStatus = p.getStatus().name();
                    }

                    ContractorBillingResponse res = ContractorBillingResponse.builder()
                            .contractorId(contractor.getId())
                            .contractorName(contractor.getContractorName())
                            .contractorEmail(contractor.getEmail())
                            .contractorMobile(contractor.getMobileNumber())
                            .totalHours(totalHours)
                            .totalAmount(totalAmount)
                            .projectId(project.getId())
                            .projectName(project.getName())
                            .totalLabours(attendanceList.size())
                            .billDate(billDate)
                            .billNo(billNo)

                            // ⭐ NEW FIELDS
                            .paidAmount(paidAmount)
                            .billStatus(billStatus)

                            .build();

                    billingList.add(res);
                }
            }
        }

        return new ApiResponse<>(true, "Billing report fetched successfully", billingList);
    }


    @Override
    public List<LabourBillingDetailsResponse> getLabourBillingDetails(
            Long contractorId, Long projectId, LocalDate fromDate, LocalDate toDate) {

        Contractor contractor = contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found"));

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<Labour> labours = labourRepository.findByProjects(project);

        List<LabourBillingDetailsResponse> responseList = new ArrayList<>();

        for (Labour labour : labours) {

            if (!labour.getContractor().getId().equals(contractorId)) continue;

            List<LabourAttendance> attendances = attendanceRepository
                    .findByLabourAndAttendanceDateBetween(labour, fromDate, toDate);

            boolean hasCheckedAttendance = attendances.stream()
                    .anyMatch(a -> Boolean.TRUE.equals(a.getIsCheck()));

            if (!hasCheckedAttendance) continue;

            long totalDays = attendances.stream()
                    .filter(LabourAttendance::getIsPresent)
                    .count();

            double totalHours = attendances.stream()
                    .mapToDouble(a -> {
                        if (a.getInTime() != null && a.getOutTime() != null &&
                                !a.getOutTime().isBefore(a.getInTime())) {

                            return Duration.between(a.getInTime(), a.getOutTime())
                                    .toMinutes() / 60.0;
                        }
                        return 0.0;
                    })
                    .sum();

            Double ratePerDay = labour.getRatePerDay() != null ? labour.getRatePerDay() : 0.0;
            double billAmount = 0.0;

            if (ratePerDay > 0 && totalHours > 0) {

                double h = totalHours;

                if (h >= 4.0 && h <= 5.0) {
                    billAmount = ratePerDay / 2.0;

                } else if (h >= 8.0 && h <= 9.0) {
                    billAmount = ratePerDay;

                } else if (h >= 11.0 && h <= 12.0) {
                    billAmount = ratePerDay * 1.5;

                } else if (h >= 14.0 && h <= 15.0) {
                    billAmount = ratePerDay * 2.0;

                } else {
                    billAmount = (ratePerDay / 8.0) * h; // pro-rata hourly
                }
            }

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

    @Override
    public ApiResponse<String> updateContractorPayment(PaymentRequest req) {

        // Auto status calculation function
        PaymentStatus status;

        // Existing payment search by billNo
        ContractorPayment payment = contractorPaymentRepository
                .findByBillNo(req.getBillNo())
                .orElse(null);

        if (payment == null) {
            // New Insert
            payment = new ContractorPayment();
            payment.setBillNo(req.getBillNo());
            payment.setContractor(contractorRepository.findById(req.getContractorId()).orElse(null));


            // প্রথম বার paidAmount সেট হবে সরাসরি
            payment.setPaidAmount(req.getPaidAmount());
        } else {
            // Existing → paid amount add হবে
            double newPaidAmount = payment.getPaidAmount() + req.getPaidAmount();
            payment.setPaidAmount(newPaidAmount);
        }

        // Fixed fields update
        payment.setBillDate(req.getBillDate());
        payment.setTotalAmount(req.getTotalAmount());
        payment.setPaymentDate(LocalDate.now());
        payment.setRemarks(req.getRemarks());

        // Auto payment status check
        if (payment.getPaidAmount() == 0) {
            status = PaymentStatus.NOT_PAID;
        } else if (payment.getPaidAmount() < payment.getTotalAmount()) {
            status = PaymentStatus.PARTIALLY_PAID;
        } else {
            status = PaymentStatus.FULL_PAID;
        }

        payment.setStatus(status);

        contractorPaymentRepository.save(payment);

        return new ApiResponse<>(true, "Payment updated successfully", null);
    }


    @Override
    public ApiResponse<List<ContractorPaymentResponse>> getPaymentHistory(LocalDate fromDate, LocalDate toDate) {

        // Fetch payments between date range
        List<ContractorPayment> list =
                contractorPaymentRepository.findByBillDateBetween(fromDate, toDate);

        List<ContractorPaymentResponse> response = list.stream()
                .map(p -> ContractorPaymentResponse.builder()
                        .contractorId(
                                p.getContractor() != null ? p.getContractor().getId() : null
                        )
                        .contractorName(
                                p.getContractor() != null ? p.getContractor().getContractorName() : null
                        )
                        .billNo(p.getBillNo())
                        .billDate(p.getBillDate())
                        .totalAmount(p.getTotalAmount())
                        .paidAmount(p.getPaidAmount())
                        .status(p.getStatus() != null ? p.getStatus().name() : "UNKNOWN")
                        .paymentDate(p.getPaymentDate())
                        .remarks(p.getRemarks())
                        .build()
                )
                // ⭐ Sort by most recent billDate
                .sorted(Comparator.comparing(ContractorPaymentResponse::getBillDate).reversed())
                .toList();

        return new ApiResponse<>(true, "Payment history fetched successfully", response);
    }




    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

}
