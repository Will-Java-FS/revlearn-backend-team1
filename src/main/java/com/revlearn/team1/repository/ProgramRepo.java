package com.revlearn.team1.repository;

import com.revlearn.team1.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepo extends JpaRepository<Program, Long> {
}
