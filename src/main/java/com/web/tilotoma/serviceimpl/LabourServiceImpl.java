package com.web.tilotoma.serviceimpl;

import com.web.tilotoma.dto.*;
import com.web.tilotoma.dto.response.LabourResponseDto;
import com.web.tilotoma.entity.*;
import com.web.tilotoma.exceptions.ResourceNotFoundException;
import com.web.tilotoma.repository.*;
import com.web.tilotoma.service.LabourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LabourServiceImpl implements LabourService {
    @Autowired
    LabourRepository labourRepo;
    @Autowired
    ContractorRepository contractorRepository;
    @Autowired
    LabourTypeRepository labourTypeRepository;
    @Autowired
    ProjectRepo projectRepo;
    @Autowired
    LabourAttendanceRepository attendanceRepo;
    @Autowired
    ContractorLabourRateRepository rateRepo;
    @Autowired
    ContractorRepository contractorRepo;


    public Labour addLabourUnderContractor(Long contractorId, LabourRequest req) {
        Contractor contractor = contractorRepository.findById(req.getContractorId())
                .orElseThrow(() -> new RuntimeException("Contractor not found"));
        LabourType labourType = labourTypeRepository.findById(req.getLabourTypeId())
                .orElseThrow(() -> new RuntimeException("Labour type not found"));

        double ratePerDay = req.getRatePerDay() != null ? req.getRatePerDay() : 0.0;
        double ratePerHour = ratePerDay > 0 ? ratePerDay / 8.0 : 0.0;

        Labour labour = Labour.builder()
                .labourName(req.getLabourName())
                .email(req.getEmail())
                .mobileNumber(req.getMobileNumber())
                .contractor(contractor)
                .labourType(labourType)
                .ratePerDay(ratePerDay)
                .ratePerHour(ratePerHour)
                .isActive(true)
                .build();

        // ✅ Add Projects if provided
        if (req.getProjectIds() != null && !req.getProjectIds().isEmpty()) {
            List<Project> projects = projectRepo.findAllById(req.getProjectIds());
            labour.setProjects(projects);
        }
        Labour saved = labourRepo.save(labour);

        // STEP 2: userId generate (NO helper method)
        String labourName = saved.getLabourName();

        String namePart = labourName.length() >= 3
                ? labourName.substring(0, 3).toLowerCase()
                : labourName.toLowerCase();

        String idPart = String.format("%03d", saved.getId());

        String userId = namePart + idPart;   // e.g., ram002

        saved.setLabourUserId(userId);

        // STEP 3: Save again with userId
        return labourRepo.save(saved);
    }

    @Transactional(readOnly = true)
    public List<LabourResponse> getAllLabours() {

        List<Labour> labours = labourRepo.findAll();

        return labours.stream().map(labour -> {

            // Force initialize lazy lists safely inside transaction
            if (labour.getProjects() != null) {
                labour.getProjects().size();
            }

            List<ProjectSimpleResponse> projectList =
                    labour.getProjects() != null ?
                            labour.getProjects().stream()
                                    .map(p -> ProjectSimpleResponse.builder()
                                            .id(p.getId())
                                            .name(p.getName())
                                            .location(p.getLocation())
                                            .city(p.getCity())
                                            .state(p.getState())
                                            .isActive(p.getIsActive())
                                            .build())
                                    .toList()
                            : List.of();

            return LabourResponse.builder()
                    .id(labour.getId())
                    .labourName(labour.getLabourName())
                    .labourUserId(labour.getLabourUserId())
                    .email(labour.getEmail())
                    .mobileNumber(labour.getMobileNumber())
                    .ratePerDay(labour.getRatePerDay())
                    .ratePerHour(labour.getRatePerHour())
                    .contractorId(labour.getContractor() != null ? labour.getContractor().getId() : null)
                    .contractorName(labour.getContractor() != null ? labour.getContractor().getContractorName() : null)
                    .labourTypeId(labour.getLabourType() != null ? labour.getLabourType().getId() : null)
                    .labourTypeName(labour.getLabourType() != null ? labour.getLabourType().getTypeName() : null)
                    .projects(projectList)     // <-- Added
                    .build();

        }).toList();
    }

    //contractorId wise Labour
    public List<LabourResponseDto> getLaboursByContractor(Long contractorId) {
        Contractor contractor = contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found"));

        List<Labour> labours = labourRepo.findByContractorId(contractorId);

        return labours.stream().map(labour -> LabourResponseDto.builder()
                .id(labour.getId())
                .labourName(labour.getLabourName())
                .email(labour.getEmail())
                .mobileNumber(labour.getMobileNumber())
                .isActive(labour.getIsActive())
                .createdOn(labour.getCreatedOn())

                .contractorId(contractor.getId())
                .contractorName(contractor.getContractorName())

                .labourTypeId(labour.getLabourType().getId())
                .labourTypeName(labour.getLabourType().getTypeName())

                .projects(
                        labour.getProjects() != null
                                ? labour.getProjects().stream()
                                .map(p -> LabourResponseDto.ProjectDto.builder()
                                        .id(p.getId())
                                        .name(p.getName())
                                        .build())
                                .toList()
                                : null
                )
                .build()
        ).toList();
    }


    public Labour getLabourById(Long id) {
        return labourRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Labour not found with id: " + id));
    }

    public Labour updateLabour(Long id, LabourRequest labourRequest) {
        Labour existing = getLabourById(id);

        existing.setLabourName(labourRequest.getLabourName());
        existing.setEmail(labourRequest.getEmail());
        existing.setMobileNumber(labourRequest.getMobileNumber());

        if (labourRequest.getContractorId() != null) {
            Contractor contractor = contractorRepository.findById(labourRequest.getContractorId())
                    .orElseThrow(() -> new RuntimeException("Contractor not found with id: " + labourRequest.getContractorId()));
            existing.setContractor(contractor);
        }

        if (labourRequest.getLabourTypeId() != null) {
            LabourType labourType = labourTypeRepository.findById(labourRequest.getLabourTypeId())
                    .orElseThrow(() -> new RuntimeException("Labour type not found with id: " + labourRequest.getLabourTypeId()));
            existing.setLabourType(labourType);
        }

        if (labourRequest.getProjectIds() != null && !labourRequest.getProjectIds().isEmpty()) {
            List<Project> projects = projectRepo.findAllById(labourRequest.getProjectIds());
            existing.setProjects(projects);
        }

        // ✅ ratePerDay and ratePerHour calculation
        if (labourRequest.getRatePerDay() != null) {
            double ratePerDay = labourRequest.getRatePerDay();
            double ratePerHour = ratePerDay / 8.0;
            existing.setRatePerDay(ratePerDay);
            existing.setRatePerHour(ratePerHour);
        }

        return labourRepo.save(existing);
    }



    public String deleteLabour(Long id) {
        Labour existing = getLabourById(id);
        labourRepo.delete(existing);
        return "Labour deleted successfully with ID: " + id;
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

    public List<LabourMonthlyReportDto> getMonthlyReport(Long contractorId, int year, int month) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // ✅ Get attendance for all labours under this contractor
        List<LabourAttendance> attendances =
                attendanceRepo.findByLabour_Contractor_IdAndAttendanceDateBetween(contractorId, startDate, endDate);

        if (attendances.isEmpty()) {
            return Collections.emptyList();
        }

        // ✅ Group attendance by labour
        Map<Labour, List<LabourAttendance>> grouped =
                attendances.stream().collect(Collectors.groupingBy(LabourAttendance::getLabour));

        // ✅ Get contractor entity once
        Contractor contractor = contractorRepo.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found"));

        List<LabourMonthlyReportDto> reportList = new ArrayList<>();

        for (Map.Entry<Labour, List<LabourAttendance>> entry : grouped.entrySet()) {
            Labour labour = entry.getKey();
            List<LabourAttendance> records = entry.getValue();

            long daysWorked = records.stream()
                    .filter(LabourAttendance::getIsPresent)
                    .count();

            double totalHours = records.stream()
                    .filter(r -> r.getInTime() != null && r.getOutTime() != null)
                    .mapToDouble(r -> ChronoUnit.MINUTES.between(r.getInTime(), r.getOutTime()) / 60.0)
                    .sum();

            // ✅ Fetch daily rate using your repository
            double dailyRate = rateRepo.findByContractorAndLabourType(contractor, labour.getLabourType())
                    .map(ContractorLabourRate::getDailyRate)
                    .orElse(0.0);

            double totalAmount = dailyRate * daysWorked;

            reportList.add(LabourMonthlyReportDto.builder()
                    .labourName(labour.getLabourName())
                    .labourType(labour.getLabourType().getTypeName()) // assuming LabourType has `typeName`
                    .daysWorked(daysWorked)
                    .totalHours(totalHours)
                    .dailyRate(dailyRate)
                    .totalAmount(totalAmount)
                    .contractorId(contractorId)
                    .build());
        }

        return reportList;
    }

    @Override
    public LabourType updateLabourType(Long id, LabourTypeRequest req) {
        LabourType existing = labourTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Labour type not found with id: " + id));

        if (req.getTypeName() != null && !req.getTypeName().isEmpty()) {
            existing.setTypeName(req.getTypeName());
        }

        return labourTypeRepository.save(existing);
    }

    @Override
    public LabourType toggleLabourTypeStatus(Long id, boolean isActive) {
        LabourType type = labourTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Labour type not found with id: " + id));

        type.setIsActive(isActive);
        return labourTypeRepository.save(type);
    }

    @Override
    public String deleteLabourType(Long id) {
        LabourType type = labourTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Labour type not found with id: " + id));

        labourTypeRepository.delete(type);
        return "Labour type deleted successfully with ID: " + id;
    }

}
