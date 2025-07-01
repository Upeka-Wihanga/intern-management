package com.internsystem.internmanagement.dto;

import com.internsystem.internmanagement.entity.ProjectStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectDTO {
    private Long projectId;
    private String projectName;
    private String description;
    private LocalDate startDate;
    private LocalDate targetDate;
    private ProjectStatus status;
    private Long projectManagerId;
    private String projectManagerName;
    private Long assignedTeamId;
    private String assignedTeamName;
    private String type;
    private List<ModuleDTO> modules;
}