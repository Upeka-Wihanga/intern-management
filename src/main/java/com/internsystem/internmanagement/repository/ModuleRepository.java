package com.internsystem.internmanagement.repository;

import com.internsystem.internmanagement.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<Module, Long> {
}