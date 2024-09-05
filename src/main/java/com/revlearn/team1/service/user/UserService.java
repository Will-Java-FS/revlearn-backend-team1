package com.revlearn.team1.service.user;

import com.revlearn.team1.dto.course.CourseDTO;
import com.revlearn.team1.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    Optional<User> findById(int id);

    void deleteById(int id);

    List<User> getAllUsers();

    List<CourseDTO> getEnrolledCourses(Long studentId);

    List<CourseDTO> getTaughtCourses(Long educatorId);

//    List<CourseDTO> getInstitutionCourses(Long institutionId);

    boolean checkExisting(String username);
}