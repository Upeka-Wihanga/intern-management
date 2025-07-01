package com.internsystem.internmanagement.controller;

import com.internsystem.internmanagement.dto.ProjectDTO;
import com.internsystem.internmanagement.service.ProjectService;
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

    @GetMapping
    public List<ProjectDTO> getAll() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getById(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ProjectDTO create(@RequestBody ProjectDTO dto) {
        return projectService.createProject(dto);
    }

    @PutMapping("/{id}")
    public ProjectDTO update(@PathVariable Long id, @RequestBody ProjectDTO dto) {
        return projectService.updateProject(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        projectService.deleteProject(id);
    }
}