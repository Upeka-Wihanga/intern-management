package com.internsystem.internmanagement.repository;

import com.internsystem.internmanagement.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
}