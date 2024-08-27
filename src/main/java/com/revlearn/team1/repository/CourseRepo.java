package com.revlearn.team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revlearn.team1.model.Course;

@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {
//    Optional<CourseDTO> findCourseDTOById(Long id);
}
