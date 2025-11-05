package com.web.tilotoma.serviceimpl;

import com.web.tilotoma.entity.Project;
import com.web.tilotoma.repository.ProjectRepo;
import com.web.tilotoma.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectRepo projectRepo;
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
}
