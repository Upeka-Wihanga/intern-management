package com.internsystem.internmanagement.service;

import com.internsystem.internmanagement.entity.*;
import com.internsystem.internmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Service
public class BulkUploadService {

    @Autowired
    private InternRepository internRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamMemberRepository teamMemberRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private FunctionRepository functionRepository;
    @Autowired
    private TestCaseRepository testCaseRepository;

    public void processFile(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String header = reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cols = line.split(",", -1); // -1 to keep empty columns

                String internCode = cols[0];
                String internName = cols[1];
                String internEmail = cols[2];
                String internPhone = cols[3];
                String institute = cols[4];
                String trainingStartDate = cols[5];
                String trainingEndDate = cols[6];

                String teamName = cols[7];
                String teamLeaderCode = cols[8];
                String role = cols[9];

                String projectName = cols[10];
                String projectDescription = cols[11];
                String startDate = cols[12];
                String targetDate = cols[13];
                String projectStatus = cols[14];
                String projectManagerCode = cols[15];
                String assignedTeamName = cols[16];

                String moduleName = cols[17];
                String moduleDescription = cols[18];
                String moduleStatus = cols[19];
                String ownerInternCode = cols[20];

                String functionName = cols[21];
                String functionDescription = cols[22];
                String functionStatus = cols[23];
                String developerInternCode = cols[24];

                String testCaseName = cols[25];
                String testCaseDescription = cols[26];
                String expectedOutput = cols[27];
                String actualOutput = cols[28];
                String testCaseStatus = cols[29];
                String createdByInternCode = cols[30];
                String executedByInternCode = cols[31];
                String executionDate = cols[32];
                String isAutomated = cols[33];
                String remarks = cols[34];

                try {
                    Intern intern = internRepository.findByInternCode(internCode).orElse(null);
                    if (intern == null) {
                        intern = new Intern();
                        intern.setInternCode(internCode);
                    }
                    intern.setName(internName);
                    intern.setEmail(internEmail);
                    intern.setPhone(internPhone);
                    intern.setInstitute(institute);
                    if (StringUtils.hasText(trainingStartDate)) intern.setTrainingStartDate(LocalDate.parse(trainingStartDate));
                    if (StringUtils.hasText(trainingEndDate)) intern.setTrainingEndDate(LocalDate.parse(trainingEndDate));
                    intern = internRepository.save(intern);

                    final String teamNameFinal = teamName;
                    Team team = teamRepository.findAll().stream()
                        .filter(t -> t.getTeamName().equalsIgnoreCase(teamNameFinal))
                        .findFirst()
                        .orElse(null);
                    if (team == null) {
                        team = new Team();
                        team.setTeamName(teamName);
                    }
                    if (StringUtils.hasText(teamLeaderCode)) {
                        final String teamLeaderCodeFinal = teamLeaderCode;
                        Intern teamLeader = internRepository.findByInternCode(teamLeaderCodeFinal).orElse(null);
                        if (teamLeader != null) {
                            team.setTeamLeader(teamLeader);
                        }
                    }
                    team = teamRepository.save(team);

                    if (team != null && intern != null) {
                        final Long teamIdFinal = team.getTeamId();
                        final Long internIdFinal = intern.getInternId();
                        boolean exists = teamMemberRepository.findAll().stream()
                            .anyMatch(tm -> tm.getTeam().getTeamId().equals(teamIdFinal)
                                         && tm.getIntern().getInternId().equals(internIdFinal));
                        if (!exists) {
                            TeamMember tm = new TeamMember();
                            tm.setTeam(team);
                            tm.setIntern(intern);
                            tm.setRole(role);
                            teamMemberRepository.save(tm);
                        }
                    }

                    final String projectNameFinal = projectName;
                    Project project = projectRepository.findAll().stream()
                        .filter(p -> p.getProjectName().equalsIgnoreCase(projectNameFinal))
                        .findFirst()
                        .orElse(null);
                    if (project == null) {
                        project = new Project();
                        project.setProjectName(projectName);
                    }
                    project.setDescription(projectDescription);
                    if (StringUtils.hasText(startDate)) project.setStartDate(LocalDate.parse(startDate));
                    if (StringUtils.hasText(targetDate)) project.setTargetDate(LocalDate.parse(targetDate));
                    if (StringUtils.hasText(projectStatus)) {
                        try {
                            project.setStatus(ProjectStatus.valueOf(projectStatus.toUpperCase().replace(" ", "_")));
                        } catch (Exception ignored) {}
                    }
                    if (StringUtils.hasText(projectManagerCode)) {
                        Intern projectManager = internRepository.findByInternCode(projectManagerCode).orElse(null);
                        if (projectManager != null) {
                            project.setProjectManager(projectManager);
                        }
                    }
                    if (StringUtils.hasText(assignedTeamName)) {
                        final String assignedTeamNameFinal = assignedTeamName;
                        Team assignedTeam = teamRepository.findAll().stream()
                            .filter(t -> t.getTeamName().equalsIgnoreCase(assignedTeamNameFinal))
                            .findFirst()
                            .orElse(null);
                        if (assignedTeam != null) {
                            project.setAssignedTeam(assignedTeam);
                        }
                    }
                    project = projectRepository.save(project);

                    final String moduleNameFinal = moduleName;
                    final Long projectIdFinal = project.getProjectId();
                    com.internsystem.internmanagement.entity.Module module = moduleRepository.findAll().stream()
                        .filter(m -> m.getModuleName().equalsIgnoreCase(moduleNameFinal)
                                  && m.getProject() != null
                                  && m.getProject().getProjectId().equals(projectIdFinal))
                        .findFirst()
                        .orElse(null);
                    if (module == null) {
                        module = new com.internsystem.internmanagement.entity.Module();
                        module.setModuleName(moduleName);
                        module.setProject(project);
                    }
                    module.setDescription(moduleDescription);
                    if (StringUtils.hasText(moduleStatus)) {
                        try {
                            module.setStatus(ModuleStatus.valueOf(moduleStatus.toUpperCase().replace(" ", "_")));
                        } catch (Exception ignored) {}
                    }
                    if (StringUtils.hasText(ownerInternCode)) {
                        Intern ownerIntern = internRepository.findByInternCode(ownerInternCode).orElse(null);
                        if (ownerIntern != null) {
                            module.setOwnerIntern(ownerIntern);
                        }
                    }
                    module = moduleRepository.save(module);

                    final String functionNameFinal = functionName;
                    final Long moduleIdFinal = module.getModuleId();
                    Function function = functionRepository.findAll().stream()
                        .filter(f -> f.getFunctionName().equalsIgnoreCase(functionNameFinal)
                                  && f.getModule() != null
                                  && f.getModule().getModuleId().equals(moduleIdFinal))
                        .findFirst()
                        .orElse(null);
                    if (function == null) {
                        function = new Function();
                        function.setFunctionName(functionName);
                        function.setModule(module);
                    }
                    function.setDescription(functionDescription);
                    if (StringUtils.hasText(functionStatus)) {
                        try {
                            function.setStatus(FunctionStatus.valueOf(functionStatus.toUpperCase().replace(" ", "_")));
                        } catch (Exception ignored) {}
                    }
                    if (StringUtils.hasText(developerInternCode)) {
                        Intern developerIntern = internRepository.findByInternCode(developerInternCode).orElse(null);
                        if (developerIntern != null) {
                            function.setDeveloperIntern(developerIntern);
                        }
                    }
                    function = functionRepository.save(function);

                    final String testCaseNameFinal = testCaseName;
                    final Long functionIdFinal = function.getFunctionId();
                    TestCase testCase = testCaseRepository.findAll().stream()
                        .filter(tc -> tc.getTestCaseName().equalsIgnoreCase(testCaseNameFinal)
                                   && tc.getFunction() != null
                                   && tc.getFunction().getFunctionId().equals(functionIdFinal))
                        .findFirst()
                        .orElse(null);
                    if (testCase == null) {
                        testCase = new TestCase();
                        testCase.setTestCaseName(testCaseName);
                        testCase.setFunction(function);
                    }
                    testCase.setDescription(testCaseDescription);
                    testCase.setExpectedOutput(expectedOutput);
                    testCase.setActualOutput(actualOutput);
                    if (StringUtils.hasText(testCaseStatus)) {
                        try {
                            testCase.setStatus(TestCaseStatus.valueOf(testCaseStatus.toUpperCase().replace(" ", "_")));
                        } catch (Exception ignored) {}
                    }
                    if (StringUtils.hasText(createdByInternCode)) {
                        Intern createdBy = internRepository.findByInternCode(createdByInternCode).orElse(null);
                        if (createdBy != null) {
                            testCase.setCreatedByIntern(createdBy);
                        }
                    }
                    if (StringUtils.hasText(executedByInternCode)) {
                        Intern executedBy = internRepository.findByInternCode(executedByInternCode).orElse(null);
                        if (executedBy != null) {
                            testCase.setExecutedByIntern(executedBy);
                        }
                    }
                    if (StringUtils.hasText(executionDate)) {
                        try {
                            testCase.setExecutionDate(LocalDate.parse(executionDate));
                        } catch (Exception ignored) {}
                    }
                    if (StringUtils.hasText(isAutomated)) {
                        testCase.setIsAutomated(Boolean.parseBoolean(isAutomated));
                    }
                    testCase.setRemarks(remarks);
                    testCaseRepository.save(testCase);

                } catch (Exception ex) {
                    System.err.println("Error processing row: " + line + " | " + ex.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to process file", e);
        }
    }
}