package com.web.tilotoma.serviceimpl;

import com.web.tilotoma.dto.ProjectDto;
import com.web.tilotoma.entity.labour.Contractor;
import com.web.tilotoma.entity.labour.Project;
import com.web.tilotoma.repository.ContractorRepository;
import com.web.tilotoma.repository.ProjectRepo;
import com.web.tilotoma.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectRepo projectRepo;

    @Autowired
    private ContractorRepository contractorRepository;


    @Override
    public Project getProjectById(Long id) {
            return projectRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
    }

    public Project updateProject(Long id, Project updatedProject) {
        Optional<Project> existingOpt = projectRepo.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Project not found with id: " + id);
        }

        Project existing = existingOpt.get();
        if (updatedProject.getName() != null) existing.setName(updatedProject.getName());
        if (updatedProject.getDescription() != null) existing.setDescription(updatedProject.getDescription());
        if (updatedProject.getLocation() != null) existing.setLocation(updatedProject.getLocation());
        if (updatedProject.getCity() != null) existing.setCity(updatedProject.getCity());
        if (updatedProject.getState() != null) existing.setState(updatedProject.getState());
        if (updatedProject.getPincode() != null) existing.setPincode(updatedProject.getPincode());
        if (updatedProject.getLandArea() != null) existing.setLandArea(updatedProject.getLandArea());
        if (updatedProject.getBuiltUpArea() != null) existing.setBuiltUpArea(updatedProject.getBuiltUpArea());
        if (updatedProject.getStartDate() != null) existing.setStartDate(updatedProject.getStartDate());
        if (updatedProject.getEndDate() != null) existing.setEndDate(updatedProject.getEndDate());
        if (updatedProject.getPossessionDate() != null) existing.setPossessionDate(updatedProject.getPossessionDate());
        if (updatedProject.getProjectType() != null) existing.setProjectType(updatedProject.getProjectType());
        if (updatedProject.getProjectStatus() != null) existing.setProjectStatus(updatedProject.getProjectStatus());
        if (updatedProject.getTotalNumberOfFlats() != null) existing.setTotalNumberOfFlats(updatedProject.getTotalNumberOfFlats());
        if (updatedProject.getTotalNumberOfFloors() != null) existing.setTotalNumberOfFloors(updatedProject.getTotalNumberOfFloors());
        if (updatedProject.getBudgetEstimate() != null) existing.setBudgetEstimate(updatedProject.getBudgetEstimate());
        if (updatedProject.getRemarks() != null) existing.setRemarks(updatedProject.getRemarks());
        if (updatedProject.getApprovedByAuthority() != null) existing.setApprovedByAuthority(updatedProject.getApprovedByAuthority());
        if (updatedProject.getApprovalNumber() != null) existing.setApprovalNumber(updatedProject.getApprovalNumber());
        if (updatedProject.getIsActive() != null) existing.setIsActive(updatedProject.getIsActive());
        if (updatedProject.getContractor() != null) existing.setContractor(updatedProject.getContractor());

        return projectRepo.save(existing);
    }

    public String deleteProject(Long id) {
        if (!projectRepo.existsById(id)) {
            throw new RuntimeException("Project not found with id: " + id);
        }

        projectRepo.deleteById(id);
        return "Project deleted successfully with id: " + id;
    }


    // Create Project
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


    // get all project
    public List<Project> getAllProjects() {
        return projectRepo.findAll();
    }

    public String updateProjectStatus(Long id, Boolean isActive) {
        Project project = projectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        project.setIsActive(isActive);
        projectRepo.save(project);

        String statusText = isActive ? "activated" : "deactivated";
        return "Project successfully " + statusText + " (id: " + id + ")";
    }
}
