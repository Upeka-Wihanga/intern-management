package com.internsystem.internmanagement.mapper;

import com.internsystem.internmanagement.dto.ModuleDTO;
import com.internsystem.internmanagement.dto.ProjectDTO;
import com.internsystem.internmanagement.entity.Module;
import com.internsystem.internmanagement.entity.Project;
import com.internsystem.internmanagement.entity.Intern;
import com.internsystem.internmanagement.entity.Team;
import java.util.List;

public class ProjectMapper {

    public static ProjectDTO toDTO(Project project, List<Module> allModules) {
        ProjectDTO dto = new ProjectDTO();
        dto.setProjectId(project.getProjectId());
        dto.setProjectName(project.getProjectName());
        dto.setDescription(project.getDescription());
        dto.setStartDate(project.getStartDate());
        dto.setTargetDate(project.getTargetDate());
        dto.setStatus(project.getStatus());
        if (project.getProjectManager() != null) {
            dto.setProjectManagerId(project.getProjectManager().getInternId());
            dto.setProjectManagerName(project.getProjectManager().getName());
        }
        if (project.getAssignedTeam() != null) {
            dto.setAssignedTeamId(project.getAssignedTeam().getTeamId());
            dto.setAssignedTeamName(project.getAssignedTeam().getTeamName());
        }
        // Add modules
        List<ModuleDTO> moduleDTOs = allModules.stream()
            .filter(m -> m.getProject() != null && m.getProject().getProjectId().equals(project.getProjectId()))
            .map(ModuleMapper::toDTO)
            .toList();
        dto.setModules(moduleDTOs);
        return dto;
    }

    public static Project toEntity(ProjectDTO dto, Intern manager, Team team) {
        Project project = new Project();
        project.setProjectId(dto.getProjectId());
        project.setProjectName(dto.getProjectName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setTargetDate(dto.getTargetDate());
        project.setStatus(dto.getStatus());
        project.setProjectManager(manager);
        project.setAssignedTeam(team);
        return project;
    }
}