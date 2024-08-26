package com.revlearn.team1.repository;

import com.revlearn.team1.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepo extends JpaRepository<Long, Course> {
}
