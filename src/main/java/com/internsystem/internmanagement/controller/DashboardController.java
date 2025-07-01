package com.internsystem.internmanagement.controller;

import com.internsystem.internmanagement.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public Object getSummary() {
        return dashboardService.getSummary();
    }

    @GetMapping("/projects")
    public Object getProjectStatus() {
        return dashboardService.getProjectStatus();
    }

    @GetMapping("/test-case-analytics")
    public Object getTestCaseAnalytics() {
        return dashboardService.getTestCaseAnalytics();
    }

    @GetMapping("/notifications")
    public Object getNotifications() {
        return dashboardService.getNotifications();
    }
}
