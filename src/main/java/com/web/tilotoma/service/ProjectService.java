package com.web.tilotoma.service;

import com.web.tilotoma.dto.ProjectDto;
import com.web.tilotoma.entity.labour.Project;

import java.util.List;


public interface ProjectService {
    Project getProjectById(Long id);
    public Project updateProject(Long id, Project updatedProject);
    public String deleteProject(Long id);
    public Project createProject(ProjectDto projectDto);
    public List<Project> getAllProjects() ;
    String updateProjectStatus(Long id, Boolean isActive);
}
