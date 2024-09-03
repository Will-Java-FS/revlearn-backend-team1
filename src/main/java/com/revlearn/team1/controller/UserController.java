package com.revlearn.team1.controller;

import com.revlearn.team1.dto.user.UserDTO;
import com.revlearn.team1.dto.user.DeleteUserResponse;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.user.UserServiceImp;
import com.revlearn.team1.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImp userService;

    @Autowired
    public UserController(UserServiceImp userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public @ResponseBody ResponseEntity<Map<String, Object>> login(@RequestBody UserDTO user) {
        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        if (!(userDetails instanceof User u)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid username or password."));
        }

        String encodedPassword = u.getPassword();
        if (!passwordEncoder.matches(user.getPassword(), encodedPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid username or password."));
        }
        String token = jwtUtil.generateToken(u);
        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", "Bearer " + token);
        response.put("tokenType", "Bearer");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public @ResponseBody ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            if (userService.checkExisting(user.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
            }
            User u = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(u);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("\"An error occurred while registering the user: " + e.getMessage());
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteUserResponse> deleteUserById(@PathVariable int id) {
        User user = userService.findById(id).orElseThrow(() -> new RuntimeException("Could not find user with ID: " + id));
        userService.deleteById(id);
        DeleteUserResponse response = new DeleteUserResponse(user, "User deleted successfully");
        return ResponseEntity.ok(response);
    }

    //TODO: Implement these three functions as User routes instead of Course routes
    // ie Return respective courses of Users
//    @GetMapping("/student/{id}")
//    public List<CourseDTO> getCoursesOfStudent(@PathVariable Long id) {
//        return courseService.getAllByStudentId(id);
//    }
//
//    @GetMapping("/educator/{id}")
//    public List<CourseDTO> getCoursesOfEducator(@PathVariable Long id) {
//        return courseService.getAllByEducatorId(id);
//    }
//
//    // Does not need security because an institution's course list should be
//    // publicly available
//    @GetMapping("/institution/{id}")
//    public List<CourseDTO> getCoursesOfInstitution(@PathVariable Long id) {
//        return courseService.getAllByInstitutionId(id);
//    }
}