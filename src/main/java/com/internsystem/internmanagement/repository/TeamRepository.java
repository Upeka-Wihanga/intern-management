package com.internsystem.internmanagement.repository;

import com.internsystem.internmanagement.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
