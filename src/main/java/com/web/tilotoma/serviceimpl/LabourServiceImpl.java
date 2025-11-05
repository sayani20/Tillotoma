package com.web.tilotoma.serviceimpl;

import com.web.tilotoma.dto.LabourRequest;
import com.web.tilotoma.dto.LabourResponse;
import com.web.tilotoma.dto.LabourTypeRequest;
import com.web.tilotoma.dto.response.LabourResponseDto;
import com.web.tilotoma.entity.Contractor;
import com.web.tilotoma.entity.Labour;
import com.web.tilotoma.entity.LabourType;
import com.web.tilotoma.entity.Project;
import com.web.tilotoma.exceptions.ResourceNotFoundException;
import com.web.tilotoma.repository.ContractorRepository;
import com.web.tilotoma.repository.LabourRepository;
import com.web.tilotoma.repository.LabourTypeRepository;
import com.web.tilotoma.repository.ProjectRepo;
import com.web.tilotoma.service.LabourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    // Add Labour Under Contractor
    public Labour addLabourUnderContractor(Long contractorId, LabourRequest req) {
        Contractor contractor = contractorRepository.findById(contractorId)
                .orElseThrow(() -> new ResourceNotFoundException("Contractor not found"));

        LabourType labourType = labourTypeRepository.findById(req.getLabourTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Labour type not found"));

        // Projects fetch
        List<Project> projects = new ArrayList<>();
        if (req.getProjectIds() != null && !req.getProjectIds().isEmpty()) {
            projects = projectRepo.findAllById(req.getProjectIds());
        }

        Labour labour = Labour.builder()
                .labourName(req.getLabourName())
                .email(req.getEmail())
                .mobileNumber(req.getMobileNumber())
                .labourType(labourType)
                .contractor(contractor)
                .projects(projects)
                .build();

        return labourRepo.save(labour);
    }

    // Get All Labours
    public List<LabourResponse> getAllLabours() {
        List<Labour> labours = labourRepo.findAll();

        return labours.stream().map(labour -> LabourResponse.builder()
                .id(labour.getId())
                .labourName(labour.getLabourName())
                .email(labour.getEmail())
                .mobileNumber(labour.getMobileNumber())
                .contractorId(labour.getContractor() != null ? labour.getContractor().getId() : null)
                .contractorName(labour.getContractor() != null ? labour.getContractor().getContractorName() : null)
                .labourTypeId(labour.getLabourType() != null ? labour.getLabourType().getId() : null)
                .labourTypeName(labour.getLabourType() != null ? labour.getLabourType().getTypeName() : null)
                .build()
        ).toList();
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

    }
