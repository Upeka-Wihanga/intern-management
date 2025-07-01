package com.internsystem.internmanagement.service;

import com.internsystem.internmanagement.dto.TeamDTO;
import com.internsystem.internmanagement.mapper.TeamMapper;
import com.internsystem.internmanagement.entity.Intern;
import com.internsystem.internmanagement.entity.Team;
import com.internsystem.internmanagement.repository.InternRepository;
import com.internsystem.internmanagement.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private InternRepository internRepository;

    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll()
                .stream()
                .map(TeamMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TeamDTO createTeam(TeamDTO dto) {
        Intern leader = internRepository.findById(dto.getTeamLeaderId())
                .orElseThrow(() -> new RuntimeException("Team Leader not found"));
        Team team = TeamMapper.toEntity(dto, leader);
        Team saved = teamRepository.save(team);
        return TeamMapper.toDTO(saved);
    }

    public Optional<TeamDTO> getTeamById(Long id) {
        return teamRepository.findById(id).map(TeamMapper::toDTO);
    }

    public TeamDTO updateTeam(Long id, TeamDTO dto) {
        Team existing = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        Intern leader = internRepository.findById(dto.getTeamLeaderId())
                .orElseThrow(() -> new RuntimeException("Team Leader not found"));

        existing.setTeamName(dto.getTeamName());
        existing.setTeamLeader(leader);

        return TeamMapper.toDTO(teamRepository.save(existing));
    }

    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }
}
