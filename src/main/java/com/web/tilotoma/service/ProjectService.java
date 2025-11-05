package com.web.tilotoma.service;

import com.web.tilotoma.entity.Project;
import org.springframework.stereotype.Service;


public interface ProjectService {
    Project getProjectById(Long id);
    public Project updateProject(Long id, Project updatedProject);
    public String deleteProject(Long id);
}
