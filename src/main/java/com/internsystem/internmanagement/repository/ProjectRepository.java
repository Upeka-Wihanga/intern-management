package com.internsystem.internmanagement.repository;

import com.internsystem.internmanagement.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}