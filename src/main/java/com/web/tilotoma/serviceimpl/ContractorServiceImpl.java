package com.web.tilotoma.serviceimpl;


import com.web.tilotoma.dto.ContractorRequest;
import com.web.tilotoma.dto.ContractorResponse;
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
import com.web.tilotoma.service.ContractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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

    public Contractor addContractor(ContractorRequest req) {
        Contractor contractor = Contractor.builder()
                .contractorName(req.getContractorName())
                .username(req.getUserName())
                .email(req.getEmail())
                .mobileNumber(req.getMobileNumber())
                .isActive(true)
                .build();

        return contractorRepository.save(contractor);
    }

    // Get All Contractors
    public List<Contractor> getAllContractors() {
        return contractorRepository.findAll();
    }


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
}
