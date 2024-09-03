package com.revlearn.team1.service.user;

import com.revlearn.team1.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(int id);

//    User save(User user);

    void deleteById(int id);

    List<User> getAllUsers();


    //TODO: Implement function(s) to return courses of User
//
//    public List<CourseDTO> getAllEnrolledCourses(Long studentId);
//
//    public List<CourseDTO> getAllTaughtCourses(Long educatorId);
//
//    public List<CourseDTO> getAllOfferedCourses(Long institutionId);
}