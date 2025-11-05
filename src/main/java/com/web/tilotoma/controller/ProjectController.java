package com.web.tilotoma.controller;

import com.web.tilotoma.dto.ApiResponse;
import com.web.tilotoma.entity.Project;
import com.web.tilotoma.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    @Autowired
    ProjectService projectService;
    @GetMapping("/idWiseProjectDetails")
    public ResponseEntity<ApiResponse<?>> getProjectById(@RequestParam("id") Long id) {
        try {
            Project project = projectService.getProjectById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Project details fetched successfully", project));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    @PutMapping("/updateProject")
    public ResponseEntity<?> updateProject(@RequestParam Long id, @RequestBody Project project) {
        try {
            Project updatedProject = projectService.updateProject(id, project);
            return ResponseEntity.ok(updatedProject);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteProject")
    public ResponseEntity<?> deleteProject(@RequestParam Long id) {
        try {
            String message = projectService.deleteProject(id);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
