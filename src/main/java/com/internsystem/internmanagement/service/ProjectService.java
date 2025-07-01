package com.internsystem.internmanagement.service;

import com.internsystem.internmanagement.dto.ProjectDTO;
import com.internsystem.internmanagement.entity.Intern;
import com.internsystem.internmanagement.entity.Module;
import com.internsystem.internmanagement.entity.Project;
import com.internsystem.internmanagement.entity.Team;
import com.internsystem.internmanagement.mapper.ProjectMapper;
import com.internsystem.internmanagement.repository.InternRepository;
import com.internsystem.internmanagement.repository.ModuleRepository;
import com.internsystem.internmanagement.repository.ProjectRepository;
import com.internsystem.internmanagement.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private InternRepository internRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    public List<ProjectDTO> getAllProjects() {
        List<Module> allModules = moduleRepository.findAll();
        return projectRepository.findAll()
                .stream()
                .map(project -> ProjectMapper.toDTO(project, allModules))
                .collect(Collectors.toList());
    }

    public Optional<ProjectDTO> getProjectById(Long id) {
        List<Module> allModules = moduleRepository.findAll();
        return projectRepository.findById(id)
                .map(project -> ProjectMapper.toDTO(project, allModules));
    }

    public ProjectDTO createProject(ProjectDTO dto) {
        Intern manager = internRepository.findById(dto.getProjectManagerId())
                .orElseThrow(() -> new RuntimeException("Project Manager not found"));
        Team team = teamRepository.findById(dto.getAssignedTeamId())
                .orElseThrow(() -> new RuntimeException("Assigned Team not found"));
        Project project = ProjectMapper.toEntity(dto, manager, team);
        Project saved = projectRepository.save(project);
        List<Module> allModules = moduleRepository.findAll();
        return ProjectMapper.toDTO(saved, allModules);
    }

    public ProjectDTO updateProject(Long id, ProjectDTO dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        Intern manager = internRepository.findById(dto.getProjectManagerId())
                .orElseThrow(() -> new RuntimeException("Project Manager not found"));
        Team team = teamRepository.findById(dto.getAssignedTeamId())
                .orElseThrow(() -> new RuntimeException("Assigned Team not found"));

        project.setProjectName(dto.getProjectName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setTargetDate(dto.getTargetDate());
        project.setStatus(dto.getStatus());
        project.setProjectManager(manager);
        project.setAssignedTeam(team);

        Project saved = projectRepository.save(project);
        List<Module> allModules = moduleRepository.findAll();
        return ProjectMapper.toDTO(saved, allModules);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}