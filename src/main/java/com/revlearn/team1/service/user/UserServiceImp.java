package com.revlearn.team1.service.user;

import com.revlearn.team1.dto.course.response.CourseResDTO;
import com.revlearn.team1.dto.user.UpdateUserRequest;
import com.revlearn.team1.enums.Roles;
import com.revlearn.team1.exceptions.UserNotFoundException;
import com.revlearn.team1.mapper.CourseMapper;
import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImp implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean checkExisting(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public void checkRole(Roles role) {
        Set<Roles> roles = Set.of(Roles.STUDENT, Roles.EDUCATOR, Roles.INSTITUTION, Roles.ADMIN);
        if (!roles.contains(role)) {
            throw new RuntimeException("Invalid Role!");
        }
    }

    @Override
    public User createUser(User user) {
        if (checkExisting(user.getUsername())) {
            throw new RuntimeException("Username Existing. Please try other username.");
        }

        if (user.getRole() == null) {
            user.setRole(Roles.STUDENT);
        } else {
            checkRole(user.getRole());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(int id, UpdateUserRequest updateUserRequest)
    {
        Optional<User> existingUser = userRepository.findById(id);
        if(existingUser.isEmpty())
        {
            throw new RuntimeException("User Not Found");
        }

        if(updateUserRequest.getOldPassword() != null && !passwordEncoder.matches(updateUserRequest.getOldPassword(), existingUser.get().getPassword()))
        {
            throw new RuntimeException("Old password is incorrect");
        }

        if(updateUserRequest.getOldPassword() != null && !updateUserRequest.getNewPassword().trim().isEmpty())
        {
            existingUser.get().setPassword(passwordEncoder.encode(updateUserRequest.getNewPassword()));
        }

        if(updateUserRequest.getFirstName() != null)
        {
            existingUser.get().setFirstName(updateUserRequest.getFirstName());
        }

        if(updateUserRequest.getLastName() != null)
        {
            existingUser.get().setLastName(updateUserRequest.getLastName());
        }
        return userRepository.save(existingUser.get());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid username."));
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<CourseResDTO> getEnrolledCourses(Long studentId) {
        // TODO: Secure so only specified student or admin-like account ("counselor"?) can retrieve data.
        // Will probably use Security context to obtain student id instead of path variable

        User student = userRepository.findById(Math.toIntExact(studentId))
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("Could not find user by id %d.", studentId)));
        return student.getEnrolledCourses().stream().map(courseMapper::toDto).toList();
    }

    @Override
    public List<CourseResDTO> getTaughtCourses(Long educatorId) {
        // TODO: Secure so only specified educator can retrieve data.
        // Will probably use Security context to obtain educator id instead of path variable

        User educator = userRepository.findById(Math.toIntExact(educatorId))
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("Could not find user by id %d.", educatorId)));
        return educator.getTaughtCourses().stream().map(courseMapper::toDto).toList();
    }

//    @Override
//    public List<CourseDTO> getInstitutionCourses(Long institutionId) {
//        // Does not need security because an institution's course list should be publicly available
//
//        User institution = userRepository.findById(Math.toIntExact(institutionId))
//                .orElseThrow(() -> new UserNotFoundException(
//                        String.format("Could not find user by id %d.", institutionId)));
//        return institution.getInstitutionCourses().stream()
//                .map(courseMapper::toDto).toList();
//    }

}
