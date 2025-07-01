package com.internsystem.internmanagement.controller;

import com.internsystem.internmanagement.dto.TeamDTO;
import com.internsystem.internmanagement.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "*")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping
    public List<TeamDTO> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getById(@PathVariable Long id) {
        return teamService.getTeamById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TeamDTO> create(@RequestBody TeamDTO dto) {
        return ResponseEntity.ok(teamService.createTeam(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamDTO> update(@PathVariable Long id, @RequestBody TeamDTO dto) {
        return ResponseEntity.ok(teamService.updateTeam(id, dto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        teamService.deleteTeam(id);
    }
}
