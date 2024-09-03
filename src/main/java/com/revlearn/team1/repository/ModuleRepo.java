package com.revlearn.team1.repository;

import com.revlearn.team1.model.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepo extends JpaRepository<CourseModule, Long> {
}
