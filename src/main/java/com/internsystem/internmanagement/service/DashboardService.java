package com.internsystem.internmanagement.service;

import com.internsystem.internmanagement.entity.Module;
import com.internsystem.internmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class DashboardService {

    @Autowired private InternRepository internRepository;
    @Autowired private TeamRepository teamRepository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private ModuleRepository moduleRepository;
    @Autowired private FunctionRepository functionRepository;
    @Autowired private TestCaseRepository testCaseRepository;

    public Map<String, Object> getSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalInterns", internRepository.count());
        summary.put("totalTeams", teamRepository.count());
        summary.put("totalProjects", projectRepository.count());
        summary.put("modulesInProgress", moduleRepository.findAll().stream().filter(m -> m.getStatus() != null && m.getStatus().name().contains("PROGRESS")).count());
        return summary;
    }

    public List<Map<String, Object>> getProjectStatus() {
        List<Map<String, Object>> result = new ArrayList<>();
        projectRepository.findAll().forEach(project -> {
            Map<String, Object> map = new HashMap<>();
            map.put("projectName", project.getProjectName());
            map.put("teamName", project.getAssignedTeam() != null ? project.getAssignedTeam().getTeamName() : null);
            map.put("pmName", project.getProjectManager() != null ? project.getProjectManager().getName() : null);
            map.put("targetDate", project.getTargetDate());

            // Progress: % of modules completed
            List<Module> modules = moduleRepository.findAll().stream()
                .filter(m -> m.getProject() != null && m.getProject().getProjectId().equals(project.getProjectId()))
                .toList();
            long total = modules.size();
            long completed = modules.stream().filter(m -> m.getStatus() != null && m.getStatus().name().equals("COMPLETED")).count();
            int progress = total > 0 ? (int) ((completed * 100.0) / total) : 0;
            map.put("progress", progress);

            map.put("status", project.getStatus() != null ? project.getStatus().name() : null);
            result.add(map);
        });
        return result;
    }

    public Map<String, Object> getTestCaseAnalytics() {
        long pass = testCaseRepository.findAll().stream().filter(tc -> tc.getStatus() != null && tc.getStatus().name().equals("PASS")).count();
        long fail = testCaseRepository.findAll().stream().filter(tc -> tc.getStatus() != null && tc.getStatus().name().equals("FAIL")).count();
        long notRun = testCaseRepository.findAll().stream().filter(tc -> tc.getStatus() != null && tc.getStatus().name().equals("NOT_RUN")).count();
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("pass", pass);
        analytics.put("fail", fail);
        analytics.put("notRun", notRun);
        return analytics;
    }

    public List<String> getNotifications() {
        List<String> notifications = new ArrayList<>();
        LocalDate today = LocalDate.now();
        projectRepository.findAll().forEach(project -> {
            if (project.getTargetDate() != null
                && project.getTargetDate().isBefore(today)
                && (project.getStatus() == null || !project.getStatus().name().equals("COMPLETED"))) {
                notifications.add("Project '" + project.getProjectName() + "' is overdue!");
            }
        });

        // Notify if any team has no leader
        teamRepository.findAll().forEach(team -> {
            if (team.getTeamLeader() == null) {
                notifications.add("Team '" + team.getTeamName() + "' has no leader assigned!");
            }
        });

        // Notify if any function has failed test cases
        functionRepository.findAll().forEach(function -> {
            long failed = testCaseRepository.findAll().stream()
                .filter(tc -> tc.getFunction() != null
                    && tc.getFunction().getFunctionId().equals(function.getFunctionId())
                    && tc.getStatus() != null
                    && tc.getStatus().name().equals("FAIL"))
                .count();
            if (failed > 0) {
                notifications.add("Function '" + function.getFunctionName() + "' has " + failed + " failed test cases!");
            }
        });

        // Add more notification logic as needed
        return notifications;
    }
}