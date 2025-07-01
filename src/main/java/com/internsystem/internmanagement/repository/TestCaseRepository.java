package com.internsystem.internmanagement.repository;

import com.internsystem.internmanagement.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
}