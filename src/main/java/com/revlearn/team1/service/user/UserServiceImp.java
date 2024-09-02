package com.revlearn.team1.service.user;

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

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean checkExisting(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    private void checkRole(String role) {
        Set<String> roles = Set.of("Student", "Educator", "Institution");
        if (!roles.contains(role)) {
            throw new RuntimeException("Invalid Role!");
        }
    }

    public User createUser(User user) {
        if (checkExisting(user.getUsername())) {
            throw new RuntimeException("Username Existing. Please try other username.");
        }

        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("Student");
        } else {
            checkRole(user.getRole());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
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

//    @Override
//    public User save(User user) {
//        return userRepository.save(user);
//    }


// TODO: Implement these three functions
//
//    @Override
//    public List<CourseDTO> getAllEnrolledCourses(Long studentId) {
//        // TODO: Secure so only specified student or admin-like account ("counselor"?)
//        // can retrieve data.
//        // Will probably use Security context to obtain student id instead of path
//        // variable
//        User student = userRepo.findById(Math.toIntExact(studentId)).orElseThrow(() -> new RuntimeException("Could not find user."));
//        return student.getEnrolledCourses().stream().map(courseMapper::toDto).toList();
//    }
//
//    @Override
//    public List<CourseDTO> getAllTaughtCourses(Long educatorId) {
//        // TODO: Secure so only specified educator can retrieve data.
//        // Will probably use Security context to obtain educator id instead of path
//        // variable
//        User educator = userRepo.findById(Math.toIntExact(educatorId)).orElseThrow(() -> new RuntimeException("Could not find user."));
//        return educator.getTaughtCourses().stream().map(courseMapper::toDto).toList();
//    }
//
//    @Override
//    public List<CourseDTO> getAllOfferedCourses(Long institutionId) {
//        User institution = userRepo.findById(Math.toIntExact(institutionId)).orElseThrow(() -> new RuntimeException("Could not find user."));
//        return institution.getInstitutionCourses().stream()
//                .map(courseMapper::toDto).toList();
//    }
}

