package com.web.tilotoma.serviceimpl;

import com.web.tilotoma.dto.LabourRequest;
import com.web.tilotoma.entity.Contractor;
import com.web.tilotoma.entity.Labour;
import com.web.tilotoma.entity.LabourType;
import com.web.tilotoma.entity.Project;
import com.web.tilotoma.repository.ContractorRepository;
import com.web.tilotoma.repository.LabourRepository;
import com.web.tilotoma.repository.LabourTypeRepository;
import com.web.tilotoma.repository.ProjectRepo;
import com.web.tilotoma.service.LabourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabourServiceImpl implements LabourService {
    @Autowired
    LabourRepository labourRepo;
    ContractorRepository contractorRepository;
    LabourTypeRepository labourTypeRepository;
    ProjectRepo projectRepo;
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

    }
