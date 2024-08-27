package com.revlearn.team1.repository;

import com.revlearn.team1.dto.CourseDTO;
import com.revlearn.team1.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {
//    Optional<CourseDTO> findCourseDTOById(Long id);
}
