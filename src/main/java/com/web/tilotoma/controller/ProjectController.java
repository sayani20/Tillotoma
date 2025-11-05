package com.web.tilotoma.controller;

import com.web.tilotoma.dto.ApiResponse;
import com.web.tilotoma.dto.ProjectDto;
import com.web.tilotoma.entity.Project;
import com.web.tilotoma.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // ✅ Create Project
    @PostMapping("/createProject")
    public ResponseEntity<ApiResponse<Project>> createProject(@RequestBody ProjectDto projectDto) {
        try {
            Project project = projectService.createProject(projectDto);
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Project created successfully",
                    project
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to create project", null));
        }
    }

    // ✅ Get All Projects
    @GetMapping("/getAllProjects")
    public ResponseEntity<ApiResponse<List<Project>>> getAllProjects() {
        try {
            List<Project> projects = projectService.getAllProjects();
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "All projects fetched successfully",
                    projects
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to fetch all projects", null));
        }
    }

    // ✅ Get Project by ID
    @GetMapping("/idWiseProjectDetails")
    public ResponseEntity<ApiResponse<Project>> getProjectById(@RequestParam("id") Long id) {
        try {
            Project project = projectService.getProjectById(id);
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Project details fetched successfully",
                    project
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to fetch project details", null));
        }
    }

    // ✅ Update Project
    @PutMapping("/updateProject")
    public ResponseEntity<ApiResponse<Project>> updateProject(@RequestParam Long id, @RequestBody Project project) {
        try {
            Project updatedProject = projectService.updateProject(id, project);
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Project updated successfully",
                    updatedProject
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to update project", null));
        }
    }

    // ✅ Delete Project
    @DeleteMapping("/deleteProject")
    public ResponseEntity<ApiResponse<String>> deleteProject(@RequestParam Long id) {
        try {
            String message = projectService.deleteProject(id);
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    message,
                    null
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to delete project", null));
        }
    }
}
